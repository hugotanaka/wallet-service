package com.hugotanaka.wallet.adapter.output.persistence.repository;

import com.hugotanaka.wallet.adapter.output.persistence.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {

    Optional<WalletEntity> findByUserId(String userId);
}
