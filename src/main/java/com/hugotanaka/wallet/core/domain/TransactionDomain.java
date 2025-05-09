package com.hugotanaka.wallet.core.domain;

import com.hugotanaka.wallet.core.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDomain {
    private UUID id;
    private UUID sourceWalletId;
    private UUID targetWalletId;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDateTime createdAt;
}
