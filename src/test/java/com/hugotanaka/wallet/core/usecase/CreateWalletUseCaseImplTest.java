package com.hugotanaka.wallet.core.usecase;

import com.hugotanaka.wallet.core.domain.WalletDomain;
import com.hugotanaka.wallet.core.port.output.CreateWalletPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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

    @InjectMocks
    private CreateWalletUseCaseImpl useCase;

    @Test
    public void shouldCreateWalletSuccessfully() {
        // given
        UUID userId = UUID.randomUUID();

        when(mockPort.save(any())).thenAnswer(invocation -> {
            WalletDomain arg = invocation.getArgument(0);
            return new WalletDomain(
                    UUID.randomUUID(),
                    arg.getUserId(),
                    arg.getBalance(),
                    arg.getCreatedAt(),
                    arg.getUpdatedAt()
            );
        });

        // when
        WalletDomain result = useCase.create(userId);

        // then
        assertNotNull(result.getId());
        assertEquals(userId, result.getUserId());
        assertEquals(BigDecimal.ZERO, result.getBalance());
        assertNotNull(result.getCreatedAt());
        verify(mockPort, times(1)).save(any());
    }
}
