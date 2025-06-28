package com.gigabank.models.dto.request.operation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

/**
 * DTO для запроса совершения операции со счетом
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OperationRequestDto {
    Long sourceUserId;

    @NotNull(message = "ID пользователя получателя обязательно")
    @Positive(message = "ID пользователя получателя должен быть положительным")
    Long targetUserId;

    @NotNull(message = "ID счета отправителя обязательно")
    @Positive(message = "ID счета отправителя должен быть положительным")
    Long accountId;

    @NotNull(message = "Сумма перевода обязательна")
    @Positive(message = "Сумма перевода должна быть положительной")
    BigDecimal amount;
}