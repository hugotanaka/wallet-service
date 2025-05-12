package com.hugotanaka.wallet.core.usecase;

import com.hugotanaka.wallet.core.domain.WalletDomain;
import com.hugotanaka.wallet.core.port.output.RetrieveWalletPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetrieveWalletUseCaseImplTest {

    @Mock
    private RetrieveWalletPersistencePort mockPort;

    @InjectMocks
    private RetrieveWalletUseCaseImpl useCase;

    @Test
    public void shouldRetrieveBalanceWhenWalletExists() {
        // given
        UUID id = UUID.randomUUID();
        WalletDomain domain = new WalletDomain(id, UUID.randomUUID());
        when(mockPort.findById(id)).thenReturn(Optional.of(domain));

        // when
        WalletDomain result = useCase.retrieveById(id);

        // then
        assertEquals(domain.getBalance(), result.getBalance());
        verify(mockPort, times(1)).findById(id);
    }

    @Test
    public void shouldThrowWhenWalletNotFound() {
        // given
        UUID id = UUID.randomUUID();

        when(mockPort.findById(id))
                .thenThrow(new IllegalArgumentException("Wallet not found"));

        // when and then
        assertThrows(IllegalArgumentException.class, () -> useCase.retrieveById(id));
    }

    @Test
    public void shouldRetrieveWalletByUserIdWhenWalletExists() {
        // given
        UUID userId = UUID.randomUUID();
        WalletDomain domain = new WalletDomain(UUID.randomUUID(), userId);
        when(mockPort.findByUserId(userId)).thenReturn(Optional.of(domain));

        // when
        Optional<WalletDomain> result = useCase.findByUserId(userId);

        // then
        assertTrue(result.isPresent());
        assertEquals(domain.getBalance(), result.get().getBalance());
        verify(mockPort, times(1)).findByUserId(userId);
    }
}
