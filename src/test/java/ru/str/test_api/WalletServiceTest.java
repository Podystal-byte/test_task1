package ru.str.test_api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.str.test_task.dto.WalletRequestDto;
import ru.str.test_task.eception.EntityNotFoundException;
import ru.str.test_task.eception.InsufficientFundsException;
import ru.str.test_task.model.OperationType;
import ru.str.test_task.model.Wallet;
import ru.str.test_task.repository.WalletRepository;
import ru.str.test_task.service.WalletService;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @Test
    void shouldDepositMoneySuccessfully() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(new BigDecimal("1000.00"));

        WalletRequestDto request = new WalletRequestDto();
        request.setWalletId(walletId);
        request.setOperationType(OperationType.DEPOSIT);
        request.setAmount(new BigDecimal("500.00"));

        when(walletRepository.findByWithUUID(walletId)).thenReturn(Optional.of(wallet));

        walletService.performOperation(request);

        assertEquals(new BigDecimal("1500.00"), wallet.getBalance());
        verify(walletRepository).save(wallet);
    }

    @Test
    void shouldWithdrawMoneySuccessfully() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(new BigDecimal("1000.00"));

        WalletRequestDto request = new WalletRequestDto();
        request.setWalletId(walletId);
        request.setOperationType(OperationType.WITHDRAW);
        request.setAmount(new BigDecimal("500.00"));

        when(walletRepository.findByWithUUID(walletId)).thenReturn(Optional.of(wallet));

        walletService.performOperation(request);

        assertEquals(new BigDecimal("500.00"), wallet.getBalance());
        verify(walletRepository).save(wallet);
    }

    @Test
    void shouldThrowExceptionWhenFundsInsufficient() {
        UUID walletId = UUID.randomUUID();
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(new BigDecimal("100.00"));

        WalletRequestDto request = new WalletRequestDto();
        request.setWalletId(walletId);
        request.setOperationType(OperationType.WITHDRAW);
        request.setAmount(new BigDecimal("500.00"));

        when(walletRepository.findByWithUUID(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(InsufficientFundsException.class, () -> {
            walletService.performOperation(request);
        });

        verify(walletRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenWalletNotFound() {
        UUID walletId = UUID.randomUUID();
        WalletRequestDto request = new WalletRequestDto();
        request.setWalletId(walletId);
        request.setOperationType(OperationType.DEPOSIT);
        request.setAmount(BigDecimal.TEN);

        when(walletRepository.findByWithUUID(walletId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            walletService.performOperation(request);
        });
    }
}