package com.hugotanaka.wallet.adapter.input.web.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BalanceResponse {
    private UUID walletId;
    private BigDecimal balance;
}
