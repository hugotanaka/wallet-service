package com.hugotanaka.wallet.core.exceptions;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String walletId) {
        super("Insufficient Balance in walletId: " + walletId);
    }
}
