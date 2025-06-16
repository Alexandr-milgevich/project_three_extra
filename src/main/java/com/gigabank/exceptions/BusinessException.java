package com.gigabank.exceptions;

/**
 * Базовый класс исключений для всех ошибок, связанных с ошибками бизнес-логики.
 * Все исключения, связанные с обработкой данных должны наследовать от этого класса.
 */
public abstract class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}