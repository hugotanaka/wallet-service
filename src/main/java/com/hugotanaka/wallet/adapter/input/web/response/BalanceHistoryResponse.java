package com.hugotanaka.wallet.adapter.input.web.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BalanceHistoryResponse {
    private UUID id;
    private UUID walletId;
    private BigDecimal balance;
    private LocalDateTime createdAt;
}
