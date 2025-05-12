package com.hugotanaka.wallet.core.port.output;

import com.hugotanaka.wallet.core.domain.WalletDomain;

import java.util.Optional;
import java.util.UUID;

public interface CreateWalletPersistencePort {

    WalletDomain save(WalletDomain wallet);

    Optional<WalletDomain> findByUserId(UUID userId);
}
