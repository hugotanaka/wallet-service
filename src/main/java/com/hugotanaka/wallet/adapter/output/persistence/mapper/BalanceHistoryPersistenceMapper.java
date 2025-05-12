package com.hugotanaka.wallet.adapter.output.persistence.mapper;

import com.hugotanaka.wallet.adapter.output.persistence.entity.BalanceHistoryEntity;
import com.hugotanaka.wallet.adapter.output.persistence.mapper.converter.MapperConverter;
import com.hugotanaka.wallet.core.domain.BalanceHistoryDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MapperConverter.class)
public interface BalanceHistoryPersistenceMapper {

    @Mapping(target = "walletId", source = "walletId", qualifiedByName = "UUIDToString")
    BalanceHistoryEntity toEntity(BalanceHistoryDomain wallet);

    @Mapping(target = "id", source = "id", qualifiedByName = "StringToUUID")
    @Mapping(target = "walletId", source = "walletId", qualifiedByName = "StringToUUID")
    BalanceHistoryDomain toDomain(BalanceHistoryEntity entity);
}
