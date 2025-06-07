package com.gigabank.constants.status;

import com.gigabank.exceptions.account.AccountValidationException;

/**
 * Перечисление, определяющее статус счета в системе.
 * Поддерживаемые типы:
 * - ACTIVE - активный
 * - BLOCKED - заблокированный
 * - ARCHIVED - архивный
 */
public enum AccountStatus {
    ACTIVE,
    BLOCKED,
    ARCHIVED;

    public static boolean isValid(String status) {
        try {
            AccountStatus.valueOf(status);
            return true;
        } catch (AccountValidationException e) {
            return false;
        }
    }
}