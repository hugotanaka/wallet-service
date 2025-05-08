package com.hugotanaka.wallet.adapter.input.web.mapper;

import com.hugotanaka.wallet.adapter.input.web.response.TransactionResponse;
import com.hugotanaka.wallet.core.domain.TransactionDomain;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionWebMapper {

    TransactionResponse toResponse(TransactionDomain transaction);
}
