package com.hugotanaka.wallet.adapter.input.web.mapper;

import com.hugotanaka.wallet.adapter.input.web.response.BalanceResponse;
import com.hugotanaka.wallet.core.domain.WalletDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BalanceWebMapper {

    @Mapping(source = "id",      target = "walletId")
    BalanceResponse toResponse(WalletDomain domain);
}
