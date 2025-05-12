package com.hugotanaka.wallet.core.usecase;

import com.hugotanaka.wallet.core.domain.BalanceHistoryDomain;
import com.hugotanaka.wallet.core.port.input.RetrieveBalanceHistoryUseCase;
import com.hugotanaka.wallet.core.port.output.RetrieveBalanceHistoryPersistencePort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RetrieveBalanceHistoryUseCaseImpl implements RetrieveBalanceHistoryUseCase {

    private static final Logger log = LoggerFactory.getLogger(RetrieveBalanceHistoryUseCaseImpl.class);
    private final RetrieveBalanceHistoryPersistencePort persistencePort;

    @Override
    @Transactional(readOnly = true)
    public List<BalanceHistoryDomain> retrieveHistoryPerPeriod(UUID walletId, LocalDateTime start, LocalDateTime end) {
        log.info("c=RetrieveBalanceHistoryUseCaseImpl, m=retrieveHistoryPerPeriod, msg=Retrieving balance history " +
                "for walletId={}, start={}, end={}", walletId, start, end);

        List<BalanceHistoryDomain> result = persistencePort.findByWalletIdAndCreatedAtBetween(walletId, start, end);

        log.info("c=RetrieveBalanceHistoryUseCaseImpl, m=retrieveHistoryPerPeriod, msg=Retrieved {} history records " +
                "for walletId={}", result.size(), walletId);

        return result;
    }
}
