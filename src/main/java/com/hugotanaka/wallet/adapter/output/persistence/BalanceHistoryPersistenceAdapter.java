package com.hugotanaka.wallet.adapter.output.persistence;

import com.hugotanaka.wallet.adapter.output.persistence.mapper.BalanceHistoryPersistenceMapper;
import com.hugotanaka.wallet.adapter.output.persistence.repository.BalanceHistoryRepository;
import com.hugotanaka.wallet.core.domain.BalanceHistoryDomain;
import com.hugotanaka.wallet.core.port.output.CreateBalanceHistoryPersistencePort;
import com.hugotanaka.wallet.core.port.output.RetrieveBalanceHistoryPersistencePort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BalanceHistoryPersistenceAdapter implements RetrieveBalanceHistoryPersistencePort, CreateBalanceHistoryPersistencePort {

    private static final Logger log = LoggerFactory.getLogger(BalanceHistoryPersistenceAdapter.class);

    private final BalanceHistoryRepository balanceHistoryRepository;
    private final BalanceHistoryPersistenceMapper balanceHistoryPersistenceMapper;

    @Override
    public List<BalanceHistoryDomain> findByWalletIdAndCreatedAtBetween(
            UUID walletId,
            LocalDateTime start,
            LocalDateTime end
    ) {
        log.info("c=BalanceHistoryPersistenceAdapter, m=findByWalletIdAndCreatedAtBetween, msg=Finding balance history by " +
                "filters, id={}, start={}, end={}", walletId, start, end);
        return balanceHistoryRepository.findByWalletIdAndCreatedAtBetween(walletId.toString(), start, end)
                .stream()
                .map(balanceHistoryPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public BalanceHistoryDomain save(BalanceHistoryDomain balanceHistoryDomain) {
        log.info("c=BalanceHistoryPersistenceAdapter, m=save, msg=Saving balance history: {}", balanceHistoryDomain);
        return balanceHistoryPersistenceMapper.toDomain(
                balanceHistoryRepository.save(
                        balanceHistoryPersistenceMapper.toEntity(balanceHistoryDomain)
                )
        );
    }
}
