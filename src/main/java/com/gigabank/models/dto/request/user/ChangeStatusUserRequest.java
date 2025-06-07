package com.gigabank.models.dto.request.user;

import com.gigabank.constants.status.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * DTO для изменения статуса пользователя.
 * Содержит обязательные данные для изменения статуса.
 */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeStatusUserRequest {
    @NotNull(message = "Статус обязателен")
    UserStatus status;

    String reason; // Причина изменения статуса (опционально)
}