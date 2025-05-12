package com.hugotanaka.wallet.adapter.input.web.mapper;

import com.hugotanaka.wallet.adapter.input.web.response.WalletResponse;
import com.hugotanaka.wallet.core.domain.WalletDomain;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WalletWebMapperTest {

    private final WalletWebMapper mapper = Mappers.getMapper(WalletWebMapper.class);

    @Test
    public void shouldMapDomainToResponse() {
        // given
        WalletDomain domain = new WalletDomain(UUID.randomUUID(), UUID.randomUUID());
        domain.setBalance(new BigDecimal("500.00"));

        // when
        WalletResponse response = mapper.toResponse(domain);

        // then
        assertNotNull(response);
        assertEquals(domain.getId(), response.getId());
        assertEquals(domain.getUserId(), response.getUserId());
        assertEquals(domain.getBalance(), response.getBalance());
        assertEquals(domain.getCreatedAt(), response.getCreatedAt());
    }
}
