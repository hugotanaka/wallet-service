package com.hugotanaka.wallet.core.port.input;

import com.hugotanaka.wallet.core.domain.WalletDomain;

import java.util.UUID;

public interface CreateWalletUseCase {

    WalletDomain create(UUID userId);
}
