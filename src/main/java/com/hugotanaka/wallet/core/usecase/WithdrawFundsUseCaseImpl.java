package com.hugotanaka.wallet.core.usecase;

import com.hugotanaka.wallet.core.domain.TransactionDomain;
import com.hugotanaka.wallet.core.domain.WalletDomain;
import com.hugotanaka.wallet.core.enums.TransactionType;
import com.hugotanaka.wallet.core.exceptions.DuplicatedWithdrawalTransactionException;
import com.hugotanaka.wallet.core.exceptions.InsufficientBalanceException;
import com.hugotanaka.wallet.core.exceptions.WalletNotFoundException;
import com.hugotanaka.wallet.core.port.input.CreateBalanceHistoryUseCase;
import com.hugotanaka.wallet.core.port.input.WithdrawFundsUseCase;
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
public class WithdrawFundsUseCaseImpl implements WithdrawFundsUseCase {

    private static final Logger log = LoggerFactory.getLogger(WithdrawFundsUseCaseImpl.class);

    private final RetrieveWalletPersistencePort retrieveWalletPersistencePort;
    private final CreateWalletPersistencePort createWalletPersistencePort;
    private final CreateBalanceHistoryUseCase createBalanceHistoryUseCase;
    private final CreateTransactionPersistencePort createTransactionPersistencePort;
    private final RetrieveTransactionPersistencePort retrieveTransactionPersistencePort;

    @Override
    @Transactional
    public TransactionDomain withdraw(UUID walletId, UUID externalReferenceId, BigDecimal amount) {
        log.info("c=WithdrawFundsUseCaseImpl, m=withdraw, msg=Start withdrawal amount={} from wallet={}, " +
                "externalReferenceId={}", amount, walletId, externalReferenceId);
        long start = System.currentTimeMillis();

        WalletDomain wallet = findWalletById(walletId);

        validateDuplicatedTransaction(walletId, externalReferenceId);
        validateWalletBalance(wallet, amount);

        wallet.withdraw(amount);
        WalletDomain updatedWallet = createWalletPersistencePort.save(wallet);

        createBalanceHistoryUseCase.create(walletId, updatedWallet.getBalance());

        TransactionDomain transaction = createTransactionPersistencePort.save(
                new TransactionDomain(
                        null,
                        updatedWallet.getId(),
                        externalReferenceId,
                        amount,
                        TransactionType.WITHDRAWAL
                )
        );

        long duration = System.currentTimeMillis() - start;
        log.info("c=WithdrawFundsUseCaseImpl, m=withdraw, msg=Completed withdrawal in {}ms", duration);
        NewRelic.recordMetric("Custom/Withdraw.duration", duration);
        NewRelic.incrementCounter("Custom/Withdraw.count");

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
                    throw new DuplicatedWithdrawalTransactionException(
                            transaction.getTargetWalletId().toString(),
                            transaction.getExternalReferenceId().toString()
                    );
                });
    }

    private void validateWalletBalance(WalletDomain wallet, BigDecimal amount) {
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
    }
}
