package com.gigabank.exceptions.buisnes_logic;

import com.gigabank.exceptions.BusinessException;

/**
 * Исключение выбрасывается, если данные не проходят валидацию.
 */
public class EntityValidationException extends BusinessException {
    public EntityValidationException(Class<?> entityClass, String message) {
        super(String.format("Ошибка при валидации в классе: %s. Причина: %s", entityClass.getSimpleName(), message));
    }
}