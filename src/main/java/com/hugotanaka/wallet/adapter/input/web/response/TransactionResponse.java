package com.hugotanaka.wallet.adapter.input.web.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionResponse {
    private UUID transactionId;
    private UUID sourceWalletId;
    private UUID targetWalletId;
    private BigDecimal amount;
    private String type;
    private LocalDateTime createdAt;
}
