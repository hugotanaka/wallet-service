package com.hugotanaka.wallet.core.port.output;

import com.hugotanaka.wallet.core.domain.BalanceHistoryDomain;

public interface CreateBalanceHistoryPersistencePort {

    BalanceHistoryDomain save(BalanceHistoryDomain balanceHistoryDomain);
}
