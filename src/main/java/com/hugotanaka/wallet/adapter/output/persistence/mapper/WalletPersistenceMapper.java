package com.hugotanaka.wallet.adapter.output.persistence.mapper;

import com.hugotanaka.wallet.adapter.output.persistence.entity.WalletEntity;
import com.hugotanaka.wallet.core.domain.WalletDomain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletPersistenceMapper {

    WalletEntity toEntity(WalletDomain wallet);

    WalletDomain toDomain(WalletEntity entity);
}
