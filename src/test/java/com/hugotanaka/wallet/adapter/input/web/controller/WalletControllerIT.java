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

    private UUID createWallet(UUID userId) {
        return createWalletUseCase.create(userId).getId();
    }
}
