package com.hugotanaka.wallet.adapter.output.persistence.entity;

import com.hugotanaka.wallet.core.enums.TransactionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import java.util.UUID;

@Data
@Entity
@Table(name = "transaction")
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "id_source_wallet", nullable = false, updatable = false)
    private UUID sourceWalletId;

    @Column(name = "id_target_wallet", nullable = false, updatable = false)
    private UUID targetWalletId;

    @Column(name = "num_balance", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "ind_type", nullable = false)
    private TransactionType type;

    @CreationTimestamp
    @Column(name = "dat_creation", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
