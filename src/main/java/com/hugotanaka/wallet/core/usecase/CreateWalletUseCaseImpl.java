package com.hugotanaka.wallet.core.usecase;

import com.hugotanaka.wallet.core.domain.WalletDomain;
import com.hugotanaka.wallet.core.port.input.CreateWalletUseCase;
import com.hugotanaka.wallet.core.port.output.CreateWalletPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateWalletUseCaseImpl implements CreateWalletUseCase {

    private final CreateWalletPersistencePort persistencePort;

    @Override
    @Transactional
    public WalletDomain create(UUID userId) {
        WalletDomain wallet = new WalletDomain(
                UUID.randomUUID(),
                userId,
                BigDecimal.ZERO,
                LocalDateTime.now()
        );
        return persistencePort.save(wallet);
    }
}
