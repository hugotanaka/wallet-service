package com.hugotanaka.wallet.adapter.output.persistence.mapper;

import com.hugotanaka.wallet.adapter.output.persistence.entity.TransactionEntity;
import com.hugotanaka.wallet.core.domain.TransactionDomain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionPersistenceMapper {

    TransactionEntity toEntity(TransactionDomain transaction);

    TransactionDomain toDomain(TransactionEntity entity);
}
