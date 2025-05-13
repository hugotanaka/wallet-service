package com.hugotanaka.wallet.core.port.output;

import com.hugotanaka.wallet.core.domain.TransactionDomain;

import java.util.Optional;
import java.util.UUID;

public interface RetrieveTransactionPersistencePort {

    Optional<TransactionDomain> retrieveByTargetWalletIdAndExternalReferenceId(UUID targetWalletId, UUID externalReferenceId);

    Optional<TransactionDomain> retrieveTransferTransaction(UUID sourceWalletId, UUID targetWalletId, UUID externalReferenceId);
}
