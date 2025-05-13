package com.hugotanaka.wallet.core.port.input;

import com.hugotanaka.wallet.core.domain.TransactionDomain;

import java.math.BigDecimal;
import java.util.UUID;

public interface WithdrawFundsUseCase {

    TransactionDomain withdraw(UUID walletId, UUID externalReferenceId, BigDecimal amount);
}
