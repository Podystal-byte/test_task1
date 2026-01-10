package ru.str.test_task.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.str.test_task.dto.WalletMapper;
import ru.str.test_task.dto.WalletRequestDto;
import ru.str.test_task.dto.WalletResponseDTO;
import ru.str.test_task.service.WalletService;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final WalletMapper walletMapper;

    @PostMapping("/wallet")
    public ResponseEntity<Void> updateWallet(@Valid @RequestBody WalletRequestDto request) {
        walletService.performOperation(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/wallets/{walletId}")
    public ResponseEntity<WalletResponseDTO> getBalance(@PathVariable UUID walletId) {
        return ResponseEntity.ok(walletMapper.toResponse(walletService.getWallet(walletId)));
    }
}