package com.hugotanaka.wallet.adapter.output.persistence.mapper;

import com.hugotanaka.wallet.adapter.output.persistence.entity.WalletEntity;
import com.hugotanaka.wallet.adapter.output.persistence.mapper.converter.MapperConverter;
import com.hugotanaka.wallet.core.domain.WalletDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MapperConverter.class)
public interface WalletPersistenceMapper {

    @Mapping(target = "userId", source = "userId", qualifiedByName = "UUIDToString")
    WalletEntity toEntity(WalletDomain wallet);

    @Mapping(target = "id", source = "id", qualifiedByName = "StringToUUID")
    @Mapping(target = "userId", source = "userId", qualifiedByName = "StringToUUID")
    WalletDomain toDomain(WalletEntity entity);
}
