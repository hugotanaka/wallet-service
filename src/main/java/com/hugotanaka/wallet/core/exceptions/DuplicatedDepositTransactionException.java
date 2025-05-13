package com.hugotanaka.wallet.core.exceptions;

public class DuplicatedDepositTransactionException extends RuntimeException {
    public DuplicatedDepositTransactionException(String walletId, String externalReferenceId) {
        super("Deposit transaction already exists for walletId: " + walletId + " and externalReferenceId: " + externalReferenceId);
    }
}
