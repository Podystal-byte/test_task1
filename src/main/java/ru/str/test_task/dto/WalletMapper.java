package ru.str.test_task.dto;

import org.mapstruct.Mapper;
import ru.str.test_task.model.Wallet;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    WalletResponseDTO toResponse(Wallet wallet);
}
