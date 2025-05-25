package com.gigabank.models.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Информация о банковском счете пользователя
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountDto {
    @NotNull
    @PositiveOrZero
    Long id;

    @PositiveOrZero(message = "Баланс должен быть положительным")
    BigDecimal balance;

    @NotEmpty(message = "Счет должен быть привязан к пользователю")
    UserDto userDto;

    List<TransactionDto> listTransactionDto = new ArrayList<>();
}
