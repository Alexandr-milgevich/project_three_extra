package com.gigabank.constants.status;

import com.gigabank.exceptions.buisnes_logic.EntityValidationException;

/**
 * Перечисление, определяющее статус счета в системе.
 * Поддерживаемые типы:
 * - ACTIVE - активный
 * - BLOCKED - заблокированный
 * - ARCHIVED - архивный
 */
public enum BankAccountStatus {
    ACTIVE,
    BLOCKED,
    ARCHIVED;

    public static boolean isValid(String status) {
        try {
            BankAccountStatus.valueOf(status);
            return true;
        } catch (EntityValidationException e) {
            return false;
        }
    }
}