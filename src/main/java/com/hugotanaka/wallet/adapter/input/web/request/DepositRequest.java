package com.hugotanaka.wallet.adapter.input.web.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class DepositRequest {

    private UUID walletId;

    private BigDecimal amount;
}
