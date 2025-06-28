package com.gigabank.models.dto.request.transaction;

import com.gigabank.constants.TransactionType;
import com.gigabank.models.dto.request.account.BankAccountRequestDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO для запроса создания транзакции.
 * Содержит минимальные данные для создания новой транзакции.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTransactionRequestDto {

    @NotNull(message = "Сумма обязательна")
    @PositiveOrZero(message = "Сумма транзакции не может быть отрицательной")
    BigDecimal amount;

    @NotEmpty(message = "Тип совершаемой транзакции обязан быть указан")
    TransactionType type;

    @NotEmpty(message = "Дата и время создания транзакции должна быть указана")
    @PastOrPresent(message = "Дата и время создания транзакции не может быть в будущем.")
    LocalDateTime createdDate;

    Long sourceUserId;

    @NotNull(message = "Идентификатор пользователя не может быть пустым")
    @NotEmpty(message = "Указание идентификатора пользователя обязательно")
    Long targetUserId;

    @NotNull(message = "Указание счета обязательно")
    @NotEmpty(message = "Счет не может быть пустым")
    BankAccountRequestDto bankAccountRequestDto;
}