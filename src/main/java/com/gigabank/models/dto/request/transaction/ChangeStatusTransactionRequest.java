package com.gigabank.models.dto.request.transaction;

import com.gigabank.constants.status.TransactionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * DTO для изменения статуса транзакции.
 * Содержит обязательные данные для изменения статуса.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeStatusTransactionRequest {
    @NotNull(message = "Статус обязателен")
    TransactionStatus status;

    String reason; // Причина изменения статуса (опционально)
}