package com.hugotanaka.wallet.adapter.input.web.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateWalletRequest {
    private UUID userId;
}
