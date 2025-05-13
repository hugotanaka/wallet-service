package com.hugotanaka.wallet.adapter.input.web.controller;

import com.hugotanaka.wallet.adapter.input.web.mapper.BalanceHistoryWebMapper;
import com.hugotanaka.wallet.adapter.input.web.mapper.BalanceWebMapper;
import com.hugotanaka.wallet.adapter.input.web.mapper.TransactionWebMapper;
import com.hugotanaka.wallet.adapter.input.web.mapper.WalletWebMapper;
import com.hugotanaka.wallet.adapter.input.web.request.CreateWalletRequest;
import com.hugotanaka.wallet.adapter.input.web.request.DepositRequest;
import com.hugotanaka.wallet.adapter.input.web.request.TransferRequest;
import com.hugotanaka.wallet.adapter.input.web.request.WithdrawRequest;
import com.hugotanaka.wallet.adapter.input.web.response.BalanceHistoryResponse;
import com.hugotanaka.wallet.adapter.input.web.response.BalanceResponse;
import com.hugotanaka.wallet.adapter.input.web.response.TransactionResponse;
import com.hugotanaka.wallet.adapter.input.web.response.WalletResponse;
import com.hugotanaka.wallet.core.port.input.CreateWalletUseCase;
import com.hugotanaka.wallet.core.port.input.DepositFundsUseCase;
import com.hugotanaka.wallet.core.port.input.RetrieveBalanceHistoryUseCase;
import com.hugotanaka.wallet.core.port.input.RetrieveWalletUseCase;
import com.hugotanaka.wallet.core.port.input.TransferFundsUseCase;
import com.hugotanaka.wallet.core.port.input.WithdrawFundsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final CreateWalletUseCase createWalletUseCase;
    private final WalletWebMapper walletWebMapper;
    private final RetrieveWalletUseCase retrieveWalletUseCase;
    private final BalanceWebMapper balanceWebMapper;
    private final RetrieveBalanceHistoryUseCase retrieveBalanceHistoryUseCase;
    private final BalanceHistoryWebMapper balanceHistoryWebMapper;
    private final DepositFundsUseCase depositFundsUseCase;
    private final WithdrawFundsUseCase withdrawFundsUseCase;
    private final TransferFundsUseCase transferFundsUseCase;
    private final TransactionWebMapper transactionWebMapper;

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

    @GetMapping("/{walletId}/balances/histories")
    @ResponseStatus(HttpStatus.OK)
    public List<BalanceHistoryResponse> retrieveBalanceHistory(
            @PathVariable UUID walletId,
            @RequestParam("start")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,
            @RequestParam("end")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end
    ) {
        return balanceHistoryWebMapper.toResponseList(
                retrieveBalanceHistoryUseCase.retrieveHistoryPerPeriod(walletId, start, end)
        );
    }

    @PostMapping("/deposits")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public TransactionResponse deposit(@RequestBody DepositRequest request) {
        return transactionWebMapper.toResponse(
                depositFundsUseCase.deposit(
                        request.getWalletId(),
                        request.getAmount(),
                        request.getExternalReferenceId()
                )
        );
    }

    @PostMapping("/withdrawals")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public TransactionResponse withdraw(@RequestBody WithdrawRequest request) {
        return transactionWebMapper.toResponse(
                withdrawFundsUseCase.withdraw(
                        request.getWalletId(),
                        request.getExternalReferenceId(),
                        request.getAmount()
                )
        );
    }

    @PostMapping("/transfers")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public TransactionResponse transfer(@RequestBody TransferRequest request) {
        return transactionWebMapper.toResponse(
                transferFundsUseCase.transfer(
                        request.getSourceWalletId(),
                        request.getTargetWalletId(),
                        request.getExternalReferenceId(),
                        request.getAmount()
                )
        );
    }
}
