package com.gigabank.constants.status;

import com.gigabank.exceptions.transaction.TransactionValidationException;

/**
 * Перечисление, определяющее статус транзакции в системе.
 * Поддерживаемые типы:
 * - ACTIVE - активная
 * - BLOCKED - заблокированная
 * - ARCHIVED - архивная
 */
public enum TransactionStatus {
    ACTIVE,
    BLOCKED,
    ARCHIVED;

    public static boolean isValid(String status) {
        try {
            TransactionStatus.valueOf(status);
            return true;
        } catch (TransactionValidationException e) {
            return false;
        }
    }
}