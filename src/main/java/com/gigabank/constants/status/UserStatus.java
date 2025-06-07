package com.gigabank.constants.status;

import com.gigabank.exceptions.User.UserValidationException;

/**
 * Перечисление, определяющее статус пользователя в системе.
 * Поддерживаемые типы:
 * - ACTIVE - активный
 * - BLOCKED - заблокированный
 * - DELETED - удаленный
 */
public enum UserStatus {
    ACTIVE,
    BLOCKED,
    DELETED;

    public static boolean isValid(String status) {
        try {
            UserStatus.valueOf(status);
            return true;
        } catch (UserValidationException e) {
            return false;
        }
    }
}