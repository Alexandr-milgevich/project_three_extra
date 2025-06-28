package com.gigabank.models.dto.request.account;

import com.gigabank.constants.status.BankAccountStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * DTO для изменения статуса счета.
 * Содержит обязательные данные для изменения статуса.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeStatusBankAccountRequest {
    @NotNull(message = "Статус обязателен")
    BankAccountStatus status;

    String reason; // Причина изменения статуса (опционально)
}