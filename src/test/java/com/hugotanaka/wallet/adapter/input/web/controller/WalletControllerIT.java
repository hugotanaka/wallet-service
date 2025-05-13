package com.hugotanaka.wallet.adapter.input.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hugotanaka.wallet.IntegrationTests;
import com.hugotanaka.wallet.adapter.input.web.request.CreateWalletRequest;
import com.hugotanaka.wallet.adapter.input.web.request.DepositRequest;
import com.hugotanaka.wallet.adapter.input.web.request.TransferRequest;
import com.hugotanaka.wallet.adapter.input.web.request.WithdrawRequest;
import com.hugotanaka.wallet.core.port.input.CreateWalletUseCase;
import com.hugotanaka.wallet.core.port.input.DepositFundsUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WalletControllerIT extends IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CreateWalletUseCase createWalletUseCase;

    @Autowired
    private DepositFundsUseCase depositFundsUseCase;

    private DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE_TIME;

    private static final String BASE_URL = "/wallets";

    @Test
    public void shouldCreateWallet() throws Exception {
        // given
        CreateWalletRequest request = new CreateWalletRequest();
        request.setUserId(UUID.randomUUID());

        // when and then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userId").value(request.getUserId().toString()))
                .andExpect(jsonPath("$.balance").value(BigDecimal.ZERO.toString()))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").doesNotExist());
    }

    @Test
    public void shouldReturnZeroBalanceOnNewWallet() throws Exception {
        // given
        UUID id = createWallet(UUID.randomUUID());

        // when and then
        mockMvc.perform(get(BASE_URL + "/{walletId}/balances", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(id.toString()))
                .andExpect(jsonPath("$.balance").value(BigDecimal.ZERO.toString()));
    }

    @Test
    void shouldReturnOnlyRecordsWithinPeriod() throws Exception {
        // given
        UUID walletId = createWallet(UUID.randomUUID());
        LocalDateTime start = LocalDateTime.now().minusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        // when and then
        mockMvc.perform(get(BASE_URL + "/{walletId}/balances/histories", walletId)
                        .param("start", start.format(fmt))
                        .param("end", end.format(fmt))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].walletId").value(walletId.toString()))
                .andExpect(jsonPath("$[0].balance").value(BigDecimal.ZERO.toString()))
                .andExpect(jsonPath("$[0].createdAt").exists());
    }

    @Test
    public void shouldDepositFundsInWallet() throws Exception {
        // given
        UUID walletId = createWallet(UUID.randomUUID());
        DepositRequest request = new DepositRequest(walletId, UUID.randomUUID(), BigDecimal.TEN);

        // when and then
        mockMvc.perform(post(BASE_URL + "/deposits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldWithdrawWalletFunds() throws Exception {
        // given
        UUID walletId = createWallet(UUID.randomUUID());
        depositFunds(walletId, new BigDecimal("100.00"));
        WithdrawRequest request = new WithdrawRequest(walletId, UUID.randomUUID(), BigDecimal.TEN);

        // when and then
        mockMvc.perform(post(BASE_URL + "/withdrawals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldTransferWalletFunds() throws Exception {
        // given
        UUID sourceWalletId = createWallet(UUID.randomUUID());
        UUID targetWalletId = createWallet(UUID.randomUUID());
        depositFunds(sourceWalletId, new BigDecimal("100.00"));
        depositFunds(targetWalletId, new BigDecimal("100.00"));
        TransferRequest request = new TransferRequest(sourceWalletId, targetWalletId, UUID.randomUUID(), BigDecimal.TEN);

        // when and then
        mockMvc.perform(post(BASE_URL + "/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    private UUID createWallet(UUID userId) {
        return createWalletUseCase.create(userId).getId();
    }

    private void depositFunds(UUID walletId, BigDecimal amount) {
        depositFundsUseCase.deposit(walletId, amount, UUID.randomUUID());
    }
}
