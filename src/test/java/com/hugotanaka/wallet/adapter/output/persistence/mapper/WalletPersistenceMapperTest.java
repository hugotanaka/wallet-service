package com.hugotanaka.wallet.adapter.output.persistence.mapper;

import com.hugotanaka.wallet.adapter.output.persistence.entity.WalletEntity;
import com.hugotanaka.wallet.core.domain.WalletDomain;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WalletPersistenceMapperTest {

    private final WalletPersistenceMapper mapper = Mappers.getMapper(WalletPersistenceMapper.class);

    @Test
    void shouldMapDomainToEntity() {
        // given
        UUID id = UUID.randomUUID();
        WalletDomain domain = new WalletDomain(id, UUID.randomUUID());

        // when
        WalletEntity entity = mapper.toEntity(domain);

        // then
        assertNotNull(entity);
        assertEquals(domain.getId().toString(), entity.getId());
        assertEquals(domain.getUserId().toString(), entity.getUserId());
        assertEquals(domain.getBalance(), entity.getBalance());
        assertEquals(domain.getCreatedAt(), entity.getCreatedAt());
    }

    @Test
    void shouldMapEntityToDomain() {
        // given
        WalletEntity entity = new WalletEntity(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new BigDecimal("250.00"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when
        WalletDomain domain = mapper.toDomain(entity);

        // then
        assertNotNull(domain);
        assertEquals(UUID.fromString(entity.getId()), domain.getId());
        assertEquals(UUID.fromString(entity.getUserId()), domain.getUserId());
        assertEquals(entity.getBalance(), domain.getBalance());
        assertEquals(entity.getCreatedAt(), domain.getCreatedAt());
    }
}
