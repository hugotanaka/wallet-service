package com.hugotanaka.wallet.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceHistoryDomain {
    private UUID id;
    private UUID walletId;
    private BigDecimal balance;
    private LocalDateTime createdAt;

    public BalanceHistoryDomain(UUID id, UUID walletId, BigDecimal balance) {
        this.id = id;
        this.walletId = walletId;
        this.balance = balance;
    }
}
