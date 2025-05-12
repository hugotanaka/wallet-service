package com.hugotanaka.wallet.adapter.input.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hugotanaka.wallet.adapter.input.web.request.CreateWalletRequest;
import com.hugotanaka.wallet.core.port.input.CreateWalletUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class WalletControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CreateWalletUseCase createWalletUseCase;

    private DateTimeFormatter fmt = DateTimeFormatter.ISO_DATE_TIME;

    @Test
    public void shouldCreateWallet() throws Exception {
        // given
        CreateWalletRequest request = new CreateWalletRequest();
        request.setUserId(UUID.randomUUID());

        // when and then
        mockMvc.perform(post("/wallets")
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
        mockMvc.perform(get("/wallets/{walletId}/balances", id))
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
        mockMvc.perform(get("/wallets/{walletId}/balances/histories", walletId)
                        .param("start", start.format(fmt))
                        .param("end", end.format(fmt))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].walletId").value(walletId.toString()))
                .andExpect(jsonPath("$[0].balance").value(BigDecimal.ZERO.toString()))
                .andExpect(jsonPath("$[0].createdAt").exists());
    }

    private UUID createWallet(UUID userId) {
        return createWalletUseCase.create(userId).getId();
    }
}
