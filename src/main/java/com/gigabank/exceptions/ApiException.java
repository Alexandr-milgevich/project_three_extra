package com.gigabank.exceptions;

/**
 * Базовый класс исключений для всех ошибок, связанных с пользователями.
 * Все исключения, связанные с обработкой данных пользователя, должны
 * наследовать от этого класса.
 */
public abstract class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
