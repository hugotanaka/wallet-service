package com.hugotanaka.wallet.adapter.output.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "balance_history")
@NoArgsConstructor
@AllArgsConstructor
public class BalanceHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "id_wallet", nullable = false, updatable = false, length = 36)
    private String walletId;

    @Column(name = "num_balance", nullable = false, updatable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @CreationTimestamp
    @Column(name = "dat_creation", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
