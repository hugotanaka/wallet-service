package com.hugotanaka.wallet.adapter.input.web.controller;

import com.hugotanaka.wallet.adapter.input.web.mapper.BalanceWebMapper;
import com.hugotanaka.wallet.adapter.input.web.mapper.WalletWebMapper;
import com.hugotanaka.wallet.adapter.input.web.request.CreateWalletRequest;
import com.hugotanaka.wallet.adapter.input.web.response.BalanceResponse;
import com.hugotanaka.wallet.adapter.input.web.response.WalletResponse;
import com.hugotanaka.wallet.core.port.input.CreateWalletUseCase;
import com.hugotanaka.wallet.core.port.input.RetrieveWalletUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final CreateWalletUseCase createWalletUseCase;
    private final WalletWebMapper walletWebMapper;
    private final RetrieveWalletUseCase retrieveWalletUseCase;
    private final BalanceWebMapper balanceWebMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WalletResponse create(@RequestBody CreateWalletRequest request) {
        return walletWebMapper.toResponse(
                createWalletUseCase.create(request.getUserId())
        );
    }

    @GetMapping("/{walletId}/balances")
    @ResponseStatus(HttpStatus.OK)
    public BalanceResponse retrieveBalance(@PathVariable UUID walletId) {
        return balanceWebMapper.toResponse(
                retrieveWalletUseCase.retrieveById(walletId)
        );
    }
}
