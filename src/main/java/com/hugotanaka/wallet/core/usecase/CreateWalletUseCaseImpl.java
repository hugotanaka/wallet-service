package com.hugotanaka.wallet.core.usecase;

import com.hugotanaka.wallet.core.domain.WalletDomain;
import com.hugotanaka.wallet.core.port.input.CreateBalanceHistoryUseCase;
import com.hugotanaka.wallet.core.port.input.CreateWalletUseCase;
import com.hugotanaka.wallet.core.port.input.RetrieveWalletUseCase;
import com.hugotanaka.wallet.core.port.output.CreateWalletPersistencePort;
import com.newrelic.api.agent.NewRelic;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateWalletUseCaseImpl implements CreateWalletUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateWalletUseCaseImpl.class);

    private final CreateWalletPersistencePort createWalletPersistencePort;
    private final RetrieveWalletUseCase retrieveWalletUseCase;
    private final CreateBalanceHistoryUseCase createBalanceHistoryUseCase;

    @Override
    @Transactional
    public WalletDomain create(UUID userId) {
        log.info("c=CreateWalletUseCaseImpl, m=create, msg=Creating wallet for user: {}", userId);
        long start = System.currentTimeMillis();

        Optional<WalletDomain> existingWallet = retrieveWalletUseCase.findByUserId(userId);
        if (existingWallet.isPresent()) {
            log.warn("c=CreateWalletUseCaseImpl, m=create, msg=Wallet already exists for user: {}", userId);
            return existingWallet.get();
        }

        WalletDomain saved = createWalletPersistencePort.save(new WalletDomain(UUID.randomUUID(), userId));
        createBalanceHistoryUseCase.create(saved.getId(), saved.getBalance());

        long duration = System.currentTimeMillis() - start;
        log.info("c=CreateWalletUseCaseImpl, m=create, msg=Created Wallet id={} for userId={} in {}ms", saved.getId(), userId, duration);

        NewRelic.recordMetric("Custom/CreateWallet.duration", duration);
        NewRelic.incrementCounter("Custom/CreateWallet.count");

        return saved;
    }
}
