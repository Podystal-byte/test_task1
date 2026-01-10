package ru.str.test_task.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.str.test_task.dto.WalletRequestDto;
import ru.str.test_task.eception.EntityNotFoundException;
import ru.str.test_task.eception.InsufficientFundsException;
import ru.str.test_task.model.Wallet;
import ru.str.test_task.repository.WalletRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    @Transactional
    public void performOperation(WalletRequestDto request) {
        if(request.getAmount().doubleValue() < 0){
            throw new IllegalArgumentException("Не может быть отрицательным");
        }

        Wallet wallet = walletRepository.findByWithUUID(request.getWalletId())
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found"));

        switch (request.getOperationType()) {
            case DEPOSIT -> wallet.setBalance(wallet.getBalance().add(request.getAmount()));
            case WITHDRAW -> {
                if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
                    throw new InsufficientFundsException("Insufficient funds");
                }
                wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
            }
        }
        walletRepository.save(wallet);
    }

    @Transactional(readOnly = true)
    public Wallet getWallet(UUID walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found"));
    }
}