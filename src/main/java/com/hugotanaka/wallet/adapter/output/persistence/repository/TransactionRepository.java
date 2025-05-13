package com.hugotanaka.wallet.adapter.output.persistence.repository;

import com.hugotanaka.wallet.adapter.output.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {

    Optional<TransactionEntity> findByTargetWalletIdAndExternalReferenceId(String targetWalletId, String externalReferenceId);

    Optional<TransactionEntity> findBySourceWalletIdAndTargetWalletIdAndExternalReferenceId(
            String sourceWalletId,
            String targetWalletId,
            String externalReferenceId
    );
}
