package com.hugotanaka.wallet.adapter.input.web.mapper;

import com.hugotanaka.wallet.adapter.input.web.response.BalanceHistoryResponse;
import com.hugotanaka.wallet.core.domain.BalanceHistoryDomain;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BalanceHistoryWebMapper {

    BalanceHistoryResponse toResponse(BalanceHistoryDomain domain);

    List<BalanceHistoryResponse> toResponseList(List<BalanceHistoryDomain> domains);
}
