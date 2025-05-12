package com.hugotanaka.wallet.core.port.input;

import com.hugotanaka.wallet.core.domain.BalanceHistoryDomain;

import java.math.BigDecimal;
import java.util.UUID;

public interface CreateBalanceHistoryUseCase {

    BalanceHistoryDomain create(UUID walletId, BigDecimal balance);
}
