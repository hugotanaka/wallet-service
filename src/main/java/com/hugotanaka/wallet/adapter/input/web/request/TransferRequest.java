package com.hugotanaka.wallet.adapter.input.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    private UUID sourceWalletId;
    private UUID targetWalletId;
    private UUID externalReferenceId;
    private BigDecimal amount;
}
