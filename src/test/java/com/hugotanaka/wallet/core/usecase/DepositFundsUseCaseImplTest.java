package com.hugotanaka.wallet.core.usecase;

import com.hugotanaka.wallet.core.domain.BalanceHistoryDomain;
import com.hugotanaka.wallet.core.domain.TransactionDomain;
import com.hugotanaka.wallet.core.domain.WalletDomain;
import com.hugotanaka.wallet.core.enums.TransactionType;
import com.hugotanaka.wallet.core.port.input.CreateBalanceHistoryUseCase;
import com.hugotanaka.wallet.core.port.output.CreateTransactionPersistencePort;
import com.hugotanaka.wallet.core.port.output.CreateWalletPersistencePort;
import com.hugotanaka.wallet.core.port.output.RetrieveTransactionPersistencePort;
import com.hugotanaka.wallet.core.port.output.RetrieveWalletPersistencePort;
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
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepositFundsUseCaseImplTest {

    @Mock
    private RetrieveWalletPersistencePort mockRetrieveWallet;

    @Mock
    private CreateWalletPersistencePort mockSaveWallet;

    @Mock
    private CreateBalanceHistoryUseCase mockSaveBalanceHistory;

    @Mock
    private CreateTransactionPersistencePort mockSaveTransaction;

    @Mock
    private RetrieveTransactionPersistencePort mockRetrieveTransaction;

    @InjectMocks
    private DepositFundsUseCaseImpl useCase;


    @Test
    void shouldDepositAndRecordTransaction() {
        // given
        UUID targetWalletId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID externalReferenceId = UUID.randomUUID();
        WalletDomain before = new WalletDomain(targetWalletId, userId);
        WalletDomain after = new WalletDomain(targetWalletId, userId);
        after.deposit(new BigDecimal("100"));
        TransactionDomain transactionMock = new TransactionDomain(
                null,
                targetWalletId,
                externalReferenceId,
                new BigDecimal("100"),
                TransactionType.DEPOSIT
        );

        when(mockRetrieveWallet.findById(targetWalletId)).thenReturn(Optional.of(before));
        when(mockSaveWallet.save(any())).thenReturn(after);
        when(mockRetrieveTransaction.retrieveByTargetWalletIdAndExternalReferenceId(any(), any()))
                .thenReturn(Optional.empty());
        when(mockSaveBalanceHistory.create(any(), any())).thenReturn(new BalanceHistoryDomain());
        when(mockSaveTransaction.save(any())).thenReturn(transactionMock);

        // when
        TransactionDomain result = useCase.deposit(
                transactionMock.getTargetWalletId(),
                new BigDecimal("100"),
                transactionMock.getExternalReferenceId()
        );

        // then
        assertNotNull(result);
        assertEquals(TransactionType.DEPOSIT, result.getType());
        verify(mockRetrieveWallet, times(1)).findById(targetWalletId);
        verify(mockRetrieveTransaction, times(1))
                .retrieveByTargetWalletIdAndExternalReferenceId(targetWalletId, externalReferenceId);
        verify(mockSaveWallet, times(1)).save(any(WalletDomain.class));
        verify(mockSaveBalanceHistory, times(1))
                .create(eq(targetWalletId), eq(after.getBalance()));
        verify(mockSaveTransaction, times(1)).save(any());
    }
}
