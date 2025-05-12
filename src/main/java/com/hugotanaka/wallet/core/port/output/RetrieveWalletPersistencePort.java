package com.hugotanaka.wallet.core.port.output;

import com.hugotanaka.wallet.core.domain.WalletDomain;

import java.util.Optional;
import java.util.UUID;

public interface RetrieveWalletPersistencePort {

    Optional<WalletDomain> findById(UUID walletId);

    Optional<WalletDomain> findByUserId(UUID userId);
}
