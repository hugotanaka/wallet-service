package com.hugotanaka.wallet.core.port.output;

import com.hugotanaka.wallet.core.domain.WalletDomain;

public interface CreateWalletPersistencePort {

    WalletDomain save(WalletDomain wallet);
}
