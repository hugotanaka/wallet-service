package com.hugotanaka.wallet.core.usecase;

import com.hugotanaka.wallet.core.domain.TransactionDomain;
import com.hugotanaka.wallet.core.domain.WalletDomain;
import com.hugotanaka.wallet.core.enums.TransactionType;
import com.hugotanaka.wallet.core.exceptions.DuplicatedTransferTransactionException;
import com.hugotanaka.wallet.core.exceptions.InsufficientBalanceException;
import com.hugotanaka.wallet.core.exceptions.WalletNotFoundException;
import com.hugotanaka.wallet.core.port.input.CreateBalanceHistoryUseCase;
import com.hugotanaka.wallet.core.port.input.TransferFundsUseCase;
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
public class TransferFundsUseCaseImpl implements TransferFundsUseCase {

    private static final Logger log = LoggerFactory.getLogger(TransferFundsUseCaseImpl.class);

    private final RetrieveWalletPersistencePort retrieveWalletPersistencePort;
    private final CreateWalletPersistencePort createWalletPersistencePort;
    private final CreateBalanceHistoryUseCase createBalanceHistoryUseCase;
    private final CreateTransactionPersistencePort createTransactionPersistencePort;
    private final RetrieveTransactionPersistencePort retrieveTransactionPersistencePort;

    @Override
    @Transactional
    public TransactionDomain transfer(
            UUID sourceWalletId,
            UUID targetWalletId,
            UUID externalReferenceId,
            BigDecimal amount
    ) {
        log.info("c=TransferFundsUseCaseImpl, m=transfer, msg=Start transfer amount={} from wallet={} to wallet={}, " +
                "externalReferenceId={}", amount, sourceWalletId, targetWalletId, externalReferenceId);
        long start = System.currentTimeMillis();

        WalletDomain sourceWallet = findWalletById(sourceWalletId);
        WalletDomain targetWallet = findWalletById(targetWalletId);

        validateDuplicatedTransaction(sourceWallet.getId(), targetWallet.getId(), externalReferenceId);
        validateSourceWalletBalance(sourceWallet, amount);

        sourceWallet.withdraw(amount);
        WalletDomain updatedSource = createWalletPersistencePort.save(sourceWallet);
        createBalanceHistoryUseCase.create(sourceWalletId, updatedSource.getBalance());

        targetWallet.deposit(amount);
        WalletDomain updatedTarget = createWalletPersistencePort.save(targetWallet);
        createBalanceHistoryUseCase.create(targetWalletId, updatedTarget.getBalance());

        TransactionDomain transaction = createTransactionPersistencePort.save(
                new TransactionDomain(
                        sourceWalletId,
                        targetWalletId,
                        externalReferenceId,
                        amount,
                        TransactionType.TRANSFER
                )
        );

        long duration = System.currentTimeMillis() - start;
        log.info("c=TransferFundsUseCaseImpl, m=transfer, msg=Completed transfer in {}ms", duration);
        NewRelic.recordMetric("Custom/Transfer.duration", duration);
        NewRelic.incrementCounter("Custom/Transfer.count");

        return transaction;
    }

    private WalletDomain findWalletById(UUID walletId) {
        Optional<WalletDomain> walletOptional = retrieveWalletPersistencePort.findById(walletId);
        if (walletOptional.isEmpty()) {
            throw new WalletNotFoundException(walletId.toString());
        }

        return walletOptional.get();
    }

    private void validateDuplicatedTransaction(UUID sourceWalletId, UUID targetWalletId, UUID externalReferenceId) {
        retrieveTransactionPersistencePort.retrieveTransferTransaction(sourceWalletId, targetWalletId, externalReferenceId)
                .ifPresent(transaction -> {
                    throw new DuplicatedTransferTransactionException(
                            transaction.getSourceWalletId().toString(),
                            transaction.getTargetWalletId().toString(),
                            transaction.getExternalReferenceId().toString()
                    );
                });
    }

    private void validateSourceWalletBalance(WalletDomain sourceWallet, BigDecimal amount) {
        if (sourceWallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
    }
}
