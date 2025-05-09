package com.hugotanaka.wallet.adapter.output.persistence;

import com.hugotanaka.wallet.adapter.output.persistence.mapper.WalletPersistenceMapper;
import com.hugotanaka.wallet.adapter.output.persistence.repository.WalletRepository;
import com.hugotanaka.wallet.core.domain.WalletDomain;
import com.hugotanaka.wallet.core.port.output.CreateWalletPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletPersistenceAdapter implements CreateWalletPersistencePort {

    private final WalletRepository walletRepository;
    private final WalletPersistenceMapper walletPersistenceMapper;

    @Override
    public WalletDomain save(final WalletDomain wallet) {
        return walletPersistenceMapper.toDomain(
                walletRepository.save(
                        walletPersistenceMapper.toEntity(wallet)
                )
        );
    }
}
