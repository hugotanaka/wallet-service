package com.hugotanaka.wallet.core.usecase;

import com.hugotanaka.wallet.core.domain.TransactionDomain;
import com.hugotanaka.wallet.core.domain.WalletDomain;
import com.hugotanaka.wallet.core.enums.TransactionType;
import com.hugotanaka.wallet.core.exceptions.DuplicatedDepositTransactionException;
import com.hugotanaka.wallet.core.exceptions.WalletNotFoundException;
import com.hugotanaka.wallet.core.port.input.CreateBalanceHistoryUseCase;
import com.hugotanaka.wallet.core.port.input.DepositFundsUseCase;
import com.hugotanaka.wallet.core.port.output.CreateTransactionPersistencePort;
import com.hugotanaka.wallet.core.port.output.CreateWalletPersistencePort;
import com.hugotanaka.wallet.core.port.output.RetrieveTransactionPersistencePort;
import com.hugotanaka.wallet.core.port.output.RetrieveWalletPersistencePort;
import com.newrelic.api.agent.NewRelic;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepositFundsUseCaseImpl implements DepositFundsUseCase {

    private static final Logger log = LoggerFactory.getLogger(DepositFundsUseCaseImpl.class);

    private final RetrieveWalletPersistencePort retrieveWalletPersistencePort;
    private final CreateWalletPersistencePort createWalletPersistencePort;
    private final CreateBalanceHistoryUseCase createBalanceHistoryUseCase;
    private final CreateTransactionPersistencePort createTransactionPersistencePort;
    private final RetrieveTransactionPersistencePort retrieveTransactionPersistencePort;

    @Override
    @Transactional
    public TransactionDomain deposit(UUID walletId, BigDecimal amount, UUID externalReferenceId) {
        log.info("c=DepositFundsUseCaseImpl, m=deposit, msg=Start deposit amount={} to wallet={}, " +
                "externalReferenceId={}", amount, walletId, externalReferenceId);
        long start = System.currentTimeMillis();

        WalletDomain wallet = findWalletById(walletId);

        validateDuplicatedTransaction(walletId, externalReferenceId);

        wallet.deposit(amount);
        WalletDomain updatedWallet = createWalletPersistencePort.save(wallet);

        createBalanceHistoryUseCase.create(updatedWallet.getId(), updatedWallet.getBalance());

        TransactionDomain transaction = createTransactionPersistencePort.save(
                new TransactionDomain(
                        null,
                        updatedWallet.getId(),
                        externalReferenceId,
                        amount,
                        TransactionType.DEPOSIT
                )
        );

        long duration = System.currentTimeMillis() - start;
        log.info("c=DepositFundsUseCaseImpl, m=deposit, msg=Completed deposit in {}ms", duration);
        NewRelic.recordMetric("Custom/Deposit.duration", duration);
        NewRelic.incrementCounter("Custom/Deposit.count");

        return transaction;
    }

    private WalletDomain findWalletById(UUID walletId) {
        Optional<WalletDomain> walletOptional = retrieveWalletPersistencePort.findById(walletId);
        if (walletOptional.isEmpty()) {
            throw new WalletNotFoundException(walletId.toString());
        }

        return walletOptional.get();
    }

    private void validateDuplicatedTransaction(UUID walletId, UUID externalReferenceId) {
        retrieveTransactionPersistencePort.retrieveByTargetWalletIdAndExternalReferenceId(walletId, externalReferenceId)
                .ifPresent(transaction -> {
                    throw new DuplicatedDepositTransactionException(
                            transaction.getTargetWalletId().toString(),
                            transaction.getExternalReferenceId().toString()
                    );
                });
    }
}
