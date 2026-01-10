package ru.str.test_task.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.str.test_task.model.OperationType;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WalletRequestDto {
    @NotNull
    private UUID walletId;
    @NotNull
    private OperationType operationType;
    @NotNull
    @Positive
    private BigDecimal amount;
}
