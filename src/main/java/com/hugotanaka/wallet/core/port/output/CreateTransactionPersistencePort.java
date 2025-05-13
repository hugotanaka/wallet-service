package com.hugotanaka.wallet.core.port.output;

import com.hugotanaka.wallet.core.domain.TransactionDomain;

public interface CreateTransactionPersistencePort {

    TransactionDomain save(TransactionDomain transaction);
}
