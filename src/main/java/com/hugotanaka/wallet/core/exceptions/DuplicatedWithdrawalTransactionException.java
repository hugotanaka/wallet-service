package com.hugotanaka.wallet.core.exceptions;

public class DuplicatedWithdrawalTransactionException extends RuntimeException {
    public DuplicatedWithdrawalTransactionException(String walletId, String externalReferenceId) {
        super("Withdrawal transaction already exists for walletId: " + walletId + " and externalReferenceId: " + externalReferenceId);
    }
}
