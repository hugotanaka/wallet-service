package com.hugotanaka.wallet.core.port.output;

import com.hugotanaka.wallet.core.domain.BalanceHistoryDomain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RetrieveBalanceHistoryPersistencePort {

    List<BalanceHistoryDomain> findByWalletIdAndCreatedAtBetween(UUID walletId,
                                                                 LocalDateTime start,
                                                                 LocalDateTime end);
}
