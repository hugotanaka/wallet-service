package com.hugotanaka.wallet.core.exceptions;

public class DuplicatedTransferTransactionException extends RuntimeException {
    public DuplicatedTransferTransactionException(String sourceWalletId, String targetWalletId, String externalReferenceId) {
        super("Transfer transaction already exists for sourceWalletId: " + sourceWalletId + ", targetWalletId: " +
                targetWalletId + " and externalReferenceId: " + externalReferenceId);
    }
}
