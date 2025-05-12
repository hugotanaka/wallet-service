package com.hugotanaka.wallet.core.port.input;

import com.hugotanaka.wallet.core.domain.WalletDomain;

import java.util.Optional;
import java.util.UUID;

public interface RetrieveWalletUseCase {

    WalletDomain retrieveById(UUID walletId);

    Optional<WalletDomain> findByUserId(UUID userId);
}
