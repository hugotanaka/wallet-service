package com.hugotanaka.wallet.adapter.output.persistence;

import com.hugotanaka.wallet.adapter.output.persistence.mapper.TransactionPersistenceMapper;
import com.hugotanaka.wallet.adapter.output.persistence.repository.TransactionRepository;
import com.hugotanaka.wallet.core.domain.TransactionDomain;
import com.hugotanaka.wallet.core.port.output.CreateTransactionPersistencePort;
import com.hugotanaka.wallet.core.port.output.RetrieveTransactionPersistencePort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TransactionPersistenceAdapter implements CreateTransactionPersistencePort, RetrieveTransactionPersistencePort {

    private static final Logger log = LoggerFactory.getLogger(TransactionPersistenceAdapter.class);

    private final TransactionRepository repository;
    private final TransactionPersistenceMapper mapper;

    @Override
    public TransactionDomain save(TransactionDomain transactionDomain) {
        log.info("c=TransactionPersistenceAdapter, m=save, msg=Saving transaction: {}", transactionDomain);
        return mapper.toDomain(
                repository.save(
                        mapper.toEntity(transactionDomain)
                )
        );
    }

    @Override
    public Optional<TransactionDomain> retrieveByTargetWalletIdAndExternalReferenceId(
            UUID targetWalletId,
            UUID externalReferenceId
    ) {
        log.info("c=TransactionPersistenceAdapter, m=retrieveByTargetWalletIdAndExternalReferenceId, targetWalletId={}, " +
                "externalReferenceId={}", targetWalletId, externalReferenceId);
        return repository.findByTargetWalletIdAndExternalReferenceId(
                targetWalletId.toString(),
                externalReferenceId.toString()
        ).map(mapper::toDomain);
    }

    @Override
    public Optional<TransactionDomain> retrieveTransferTransaction(
            UUID sourceWalletId,
            UUID targetWalletId,
            UUID externalReferenceId
    ) {
        log.info("c=TransactionPersistenceAdapter, m=retrieveTransferTransaction, sourceWalletId={}, targetWalletId={}, " +
                "externalReferenceId={}", sourceWalletId, targetWalletId, externalReferenceId);
        return repository.findBySourceWalletIdAndTargetWalletIdAndExternalReferenceId(
                sourceWalletId.toString(),
                targetWalletId.toString(),
                externalReferenceId.toString()
        ).map(mapper::toDomain);
    }
}
