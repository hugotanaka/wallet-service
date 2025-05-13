package com.hugotanaka.wallet.core.port.input;

import com.hugotanaka.wallet.core.domain.TransactionDomain;

import java.math.BigDecimal;
import java.util.UUID;

public interface DepositFundsUseCase {

    TransactionDomain deposit(UUID walletId, BigDecimal amount, UUID externalReferenceId);
}
