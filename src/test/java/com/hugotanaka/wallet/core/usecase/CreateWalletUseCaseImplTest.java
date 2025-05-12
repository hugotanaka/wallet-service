package com.hugotanaka.wallet.core.usecase;

import com.hugotanaka.wallet.core.domain.WalletDomain;
import com.hugotanaka.wallet.core.port.output.CreateWalletPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateWalletUseCaseImplTest {

    @Mock
    private CreateWalletPersistencePort mockPort;

    @Mock
    private RetrieveWalletUseCaseImpl retrieveWalletUseCase;

    @InjectMocks
    private CreateWalletUseCaseImpl createWalletUseCase;

    @Test
    public void shouldCreateWalletSuccessfully() {
        // given
        UUID userId = UUID.randomUUID();
        when(mockPort.save(any())).thenReturn(new WalletDomain(UUID.randomUUID(), userId));
        when(retrieveWalletUseCase.findByUserId(userId)).thenReturn(Optional.empty());

        // when
        WalletDomain result = createWalletUseCase.create(userId);

        // then
        assertNotNull(result.getId());
        assertEquals(userId, result.getUserId());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        assertNotNull(result.getCreatedAt());
        verify(retrieveWalletUseCase, times(1)).findByUserId(any());
        verify(mockPort, times(1)).save(any());
    }

    @Test
    public void shouldRetrieveWalletWhenAlreadyExists() {
        // given
        UUID userId = UUID.randomUUID();
        WalletDomain existingWallet = new WalletDomain(UUID.randomUUID(), userId);
        when(retrieveWalletUseCase.findByUserId(userId)).thenReturn(Optional.of(existingWallet));

        // when
        WalletDomain result = createWalletUseCase.create(userId);

        // then
        assertEquals(existingWallet, result);
        verify(retrieveWalletUseCase, times(1)).findByUserId(any());
        verify(mockPort, times(0)).save(any());
    }
}
