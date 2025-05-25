package com.gigabank.models.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

/**
 * Возврат по транзакции
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefundDto {
    @NotNull
    @PositiveOrZero
    Long id;

    @NotNull(message = "Сумма обязательна")
    @PositiveOrZero(message = "Число не может быть отрицательным")
    BigDecimal amount;

    String description;

    @NotEmpty(message = "Должен быть идентификатор транзакции")
    String transactionUuid;
}
