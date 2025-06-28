package com.gigabank.models.dto.request.operation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

/**
 * DTO для запроса перевода между счетами
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransferRequestDto {
    @NotNull(message = "ID пользователя отправителя обязательно")
    @Positive(message = "ID пользователя отправителя должен быть положительным")
    Long sourceUserId;

    @NotNull(message = "ID пользователя получателя обязательно")
    @Positive(message = "ID пользователя получателя должен быть положительным")
    Long targetUserId;

    @NotNull(message = "ID счета отправителя обязательно")
    @Positive(message = "ID счета отправителя должен быть положительным")
    Long fromAccountId;

    @NotNull(message = "ID счета получателя обязательно")
    @Positive(message = "ID счета получателя должен быть положительным")
    Long toAccountId;

    @NotNull(message = "Сумма перевода обязательна")
    @Positive(message = "Сумма перевода должна быть положительной")
    BigDecimal amount;
}