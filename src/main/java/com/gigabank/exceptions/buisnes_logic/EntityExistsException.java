package com.gigabank.exceptions.buisnes_logic;

import com.gigabank.exceptions.BusinessException;

/**
 * Исключение выбрасывается, когда при создании
 * по данному идентификатору уже существует такой объект.
 */
public class EntityExistsException extends BusinessException {
    public EntityExistsException(Class<?> entityClass, String message) {
        super(String.format("Ошибка в классе: %s. Причина: %s", entityClass.getSimpleName(), message));
    }
}