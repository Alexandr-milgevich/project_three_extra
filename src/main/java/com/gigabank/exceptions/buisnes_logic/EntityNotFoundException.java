package com.gigabank.exceptions.buisnes_logic;

import com.gigabank.exceptions.BusinessException;

/**
 * Исключение выбрасывается, когда не удается найти объект по идентификатору.
 */
public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(Class<?> entityClass, Object id) {
        super(String.format("%s not found with id: %s", entityClass.getSimpleName(), id));
    }
}