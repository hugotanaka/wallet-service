package com.hugotanaka.wallet.core.port.input;

import com.hugotanaka.wallet.core.domain.TransactionDomain;

import java.math.BigDecimal;
import java.util.UUID;

public interface TransferFundsUseCase {

    TransactionDomain transfer(UUID sourceWalletId, UUID targetWalletId, UUID externalReferenceId, BigDecimal amount);
}
