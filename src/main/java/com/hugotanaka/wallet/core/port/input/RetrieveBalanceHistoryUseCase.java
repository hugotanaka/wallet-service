package com.hugotanaka.wallet.core.port.input;


import com.hugotanaka.wallet.core.domain.BalanceHistoryDomain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RetrieveBalanceHistoryUseCase {

    List<BalanceHistoryDomain> retrieveHistoryPerPeriod(UUID walletId, LocalDateTime start, LocalDateTime end);
}
