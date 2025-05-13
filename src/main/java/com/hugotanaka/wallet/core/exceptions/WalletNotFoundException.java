package com.hugotanaka.wallet.core.exceptions;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(String walletId) {
        super("Wallet not found" + walletId);
    }
}
