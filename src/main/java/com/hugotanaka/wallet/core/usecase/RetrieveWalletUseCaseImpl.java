package com.hugotanaka.wallet.core.usecase;

import com.hugotanaka.wallet.core.domain.WalletDomain;
import com.hugotanaka.wallet.core.exceptions.WalletNotFoundException;
import com.hugotanaka.wallet.core.port.input.RetrieveWalletUseCase;
import com.hugotanaka.wallet.core.port.output.RetrieveWalletPersistencePort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RetrieveWalletUseCaseImpl implements RetrieveWalletUseCase {

    private static final Logger log = LoggerFactory.getLogger(RetrieveWalletUseCaseImpl.class);

    private final RetrieveWalletPersistencePort persistencePort;

    @Override
    @Transactional(readOnly = true)
    public WalletDomain retrieveById(UUID walletId) {
        log.info("c=RetrieveWalletUseCaseImpl, m=retrieveById, msg=Finding wallet by id: {}", walletId);

        return persistencePort.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WalletDomain> findByUserId(UUID userId) {
        log.info("c=RetrieveWalletUseCaseImpl, m=findByUserId, msg=Finding wallet by userId: {}", userId);

        return persistencePort.findByUserId(userId);
    }
}
