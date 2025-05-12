package com.hugotanaka.wallet.adapter.input.web.mapper;

import com.hugotanaka.wallet.adapter.input.web.response.BalanceHistoryResponse;
import com.hugotanaka.wallet.core.domain.BalanceHistoryDomain;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BalanceHistoryWebMapperTest {

    private final BalanceHistoryWebMapper mapper = Mappers.getMapper(BalanceHistoryWebMapper.class);

    @Test
    void shouldMapDomainToResponse() {
        // given
        BalanceHistoryDomain domain = new BalanceHistoryDomain(
                UUID.randomUUID(),
                UUID.randomUUID(),
                new BigDecimal("123.45"),
                LocalDateTime.now().minusHours(2)
        );

        // when
        BalanceHistoryResponse resp = mapper.toResponse(domain);

        // then
        assertNotNull(resp);
        assertEquals(domain.getId(), resp.getId());
        assertEquals(domain.getWalletId(), resp.getWalletId());
        assertEquals(domain.getBalance(), resp.getBalance());
        assertEquals(domain.getCreatedAt(), resp.getCreatedAt());
    }

    @Test
    void shouldMapDomainListToResponseList() {
        // given
        UUID walletId = UUID.randomUUID();
        BalanceHistoryDomain firstBalance = new BalanceHistoryDomain(
                UUID.randomUUID(),
                walletId,
                new BigDecimal("10.00"),
                LocalDateTime.now().minusHours(1)
        );

        BalanceHistoryDomain secondBalance = new BalanceHistoryDomain(
                UUID.randomUUID(),
                walletId,
                new BigDecimal("20.00"),
                LocalDateTime.now().minusHours(5)
        );

        // when
        List<BalanceHistoryResponse> list = mapper.toResponseList(List.of(firstBalance, secondBalance));

        // then
        assertEquals(2, list.size());
        assertEquals(firstBalance.getId(), list.getFirst().getId());
        assertEquals(firstBalance.getWalletId(), list.getFirst().getWalletId());
        assertEquals(firstBalance.getBalance(), list.getFirst().getBalance());
        assertEquals(firstBalance.getCreatedAt(), list.getFirst().getCreatedAt());

        assertEquals(secondBalance.getId(), list.get(1).getId());
        assertEquals(secondBalance.getWalletId(), list.get(1).getWalletId());
        assertEquals(secondBalance.getBalance(), list.get(1).getBalance());
        assertEquals(secondBalance.getCreatedAt(), list.get(1).getCreatedAt());
    }
}
