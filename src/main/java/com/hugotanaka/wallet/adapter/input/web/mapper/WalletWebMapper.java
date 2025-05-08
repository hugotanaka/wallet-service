package com.hugotanaka.wallet.adapter.input.web.mapper;

import com.hugotanaka.wallet.adapter.input.web.response.WalletResponse;
import com.hugotanaka.wallet.core.domain.WalletDomain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletWebMapper {

    WalletResponse toResponse(WalletDomain domain);
}
