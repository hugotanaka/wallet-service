package com.hugotanaka.wallet.adapter.output.persistence.mapper;

import com.hugotanaka.wallet.adapter.output.persistence.entity.WalletEntity;
import com.hugotanaka.wallet.adapter.output.persistence.mapper.converter.MapperConverter;
import com.hugotanaka.wallet.core.domain.WalletDomain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletPersistenceMapperTest {

    @Mock
    private MapperConverter converter;

    @InjectMocks
    private WalletPersistenceMapperImpl mapper;

    @Test
    public void shouldMapDomainToEntity() {
        // given
        UUID id = UUID.randomUUID();
        WalletDomain domain = new WalletDomain(id, UUID.randomUUID());
        when(converter.convertUUIDToString(any())).thenCallRealMethod();

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
    public void shouldMapEntityToDomain() {
        // given
        WalletEntity entity = new WalletEntity(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new BigDecimal("250.00"),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(converter.convertStringToUUID(any())).thenCallRealMethod();

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
