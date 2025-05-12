package com.hugotanaka.wallet.core.usecase;

import com.hugotanaka.wallet.core.domain.BalanceHistoryDomain;
import com.hugotanaka.wallet.core.port.output.RetrieveBalanceHistoryPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveBalanceHistoryUseCaseImplTest {

    @Mock
    private RetrieveBalanceHistoryPersistencePort port;

    @InjectMocks
    private RetrieveBalanceHistoryUseCaseImpl useCase;

    @Test
    void shouldReturnEmptyListWhenNoRecords() {
        // given
        UUID walletId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now().minusDays(2);
        LocalDateTime end = LocalDateTime.now();

        when(port.findByWalletIdAndCreatedAtBetween(walletId, start, end))
                .thenReturn(List.of());

        // when
        List<BalanceHistoryDomain> result = useCase.retrieveHistoryPerPeriod(walletId, start, end);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(port, times(1)).findByWalletIdAndCreatedAtBetween(walletId, start, end);
    }

    @Test
    void shouldMapEntitiesToDomainList() {
        // given
        UUID walletId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();

        var firstBalance = new BalanceHistoryDomain(
                UUID.randomUUID(), walletId, new BigDecimal("50"), start.plusHours(1));
        var secondBalance = new BalanceHistoryDomain(
                UUID.randomUUID(), walletId, new BigDecimal("75"), start.plusHours(5));

        when(port.findByWalletIdAndCreatedAtBetween(walletId, start, end))
                .thenReturn(List.of(firstBalance, secondBalance));

        // when
        List<BalanceHistoryDomain> result = useCase.retrieveHistoryPerPeriod(walletId, start, end);

        // then
        assertEquals(2, result.size());

        BalanceHistoryDomain d1 = result.getFirst();
        assertEquals(firstBalance.getId(), d1.getId());
        assertEquals(firstBalance.getWalletId(), d1.getWalletId());
        assertEquals(firstBalance.getCreatedAt(), d1.getCreatedAt());
        assertEquals(firstBalance.getBalance(), d1.getBalance());

        BalanceHistoryDomain d2 = result.get(1);
        assertEquals(secondBalance.getId(), d2.getId());
        assertEquals(secondBalance.getWalletId(), d2.getWalletId());
        assertEquals(secondBalance.getCreatedAt(), d2.getCreatedAt());
        assertEquals(secondBalance.getBalance(), d2.getBalance());

        verify(port, times(1)).findByWalletIdAndCreatedAtBetween(walletId, start, end);
    }
}
