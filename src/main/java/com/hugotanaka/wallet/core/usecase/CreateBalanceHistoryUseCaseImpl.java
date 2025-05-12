package com.hugotanaka.wallet.core.usecase;

import com.hugotanaka.wallet.core.domain.BalanceHistoryDomain;
import com.hugotanaka.wallet.core.port.input.CreateBalanceHistoryUseCase;
import com.hugotanaka.wallet.core.port.output.CreateBalanceHistoryPersistencePort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateBalanceHistoryUseCaseImpl implements CreateBalanceHistoryUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateBalanceHistoryUseCaseImpl.class);

    private final CreateBalanceHistoryPersistencePort createWalletPersistencePort;

    @Override
    @Transactional
    public BalanceHistoryDomain create(UUID walletId, BigDecimal balance) {
        log.info("c=CreateBalanceHistoryUseCaseImpl, m=create, msg=Creating balance history for wallet: {}", walletId);
        long start = System.currentTimeMillis();

        BalanceHistoryDomain saved = createWalletPersistencePort.save(new BalanceHistoryDomain(UUID.randomUUID(), walletId, balance));

        long duration = System.currentTimeMillis() - start;
        log.info("c=CreateBalanceHistoryUseCaseImpl, m=create, msg=Created balance history for walletId={} in {}ms", walletId, duration);

        return saved;
    }
}
