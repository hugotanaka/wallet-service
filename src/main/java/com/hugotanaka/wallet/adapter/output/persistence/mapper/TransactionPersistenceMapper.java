package com.hugotanaka.wallet.adapter.output.persistence.mapper;

import com.hugotanaka.wallet.adapter.output.persistence.entity.TransactionEntity;
import com.hugotanaka.wallet.adapter.output.persistence.mapper.converter.MapperConverter;
import com.hugotanaka.wallet.core.domain.TransactionDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MapperConverter.class)
public interface TransactionPersistenceMapper {

    @Mapping(target = "sourceWalletId", source = "sourceWalletId", qualifiedByName = {"MapperConverter", "UUIDToString"})
    @Mapping(target = "targetWalletId", source = "targetWalletId", qualifiedByName = {"MapperConverter", "UUIDToString"})
    TransactionEntity toEntity(TransactionDomain transaction);

    @Mapping(target = "id", source = "id", qualifiedByName = {"MapperConverter", "StringToUUID"})
    @Mapping(target = "sourceWalletId", source = "sourceWalletId", qualifiedByName = {"MapperConverter", "StringToUUID"})
    @Mapping(target = "targetWalletId", source = "targetWalletId", qualifiedByName = {"MapperConverter", "StringToUUID"})
    TransactionDomain toDomain(TransactionEntity entity);
}
