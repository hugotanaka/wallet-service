package com.hugotanaka.wallet.adapter.output.persistence.repository;

import com.hugotanaka.wallet.adapter.output.persistence.entity.BalanceHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BalanceHistoryRepository extends JpaRepository<BalanceHistoryEntity, String> {

    List<BalanceHistoryEntity> findByWalletIdAndCreatedAtBetween(
            String walletId,
            LocalDateTime start,
            LocalDateTime end
    );
}
