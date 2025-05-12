package com.hugotanaka.wallet.adapter.output.persistence;

import com.hugotanaka.wallet.adapter.output.persistence.mapper.WalletPersistenceMapper;
import com.hugotanaka.wallet.adapter.output.persistence.repository.WalletRepository;
import com.hugotanaka.wallet.core.domain.WalletDomain;
import com.hugotanaka.wallet.core.port.output.CreateWalletPersistencePort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WalletPersistenceAdapter implements CreateWalletPersistencePort {

    private static final Logger log = LoggerFactory.getLogger(WalletPersistenceAdapter.class);

    private final WalletRepository walletRepository;
    private final WalletPersistenceMapper walletPersistenceMapper;

    @Override
    public WalletDomain save(final WalletDomain wallet) {
        log.info("c=WalletPersistenceAdapter, m=save, msg=Saving wallet: {}", wallet);
        return walletPersistenceMapper.toDomain(
                walletRepository.save(
                        walletPersistenceMapper.toEntity(wallet)
                )
        );
    }

    @Override
    public Optional<WalletDomain> findByUserId(UUID userId) {
        log.info("c=WalletPersistenceAdapter, m=findByUserId, msg=Finding wallet by userId: {}", userId);
        return walletRepository.findByUserId(userId.toString())
                .map(walletPersistenceMapper::toDomain);
    }
}
