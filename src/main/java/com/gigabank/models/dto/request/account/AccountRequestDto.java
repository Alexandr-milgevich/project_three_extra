package com.gigabank.models.dto.request.account;

import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.models.dto.response.UserResponseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO для запроса по работе со счетом.
 * Содержит данные для совершения операций со счетом.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountRequestDto {
    @NotNull
    @PositiveOrZero
    Long id;

    @NotBlank(message = "Счет не может быть пустым")
    String numberAccount;

    @PositiveOrZero(message = "Баланс должен быть положительным")
    BigDecimal balance;

    @NotEmpty(message = "Счет должен быть привязан к пользователю")
    UserResponseDto userResponseDto;

    @NotEmpty(message = "У счета должен быть список транзакций")
    List<TransactionResponseDto> listTransactionResponseDto;
}