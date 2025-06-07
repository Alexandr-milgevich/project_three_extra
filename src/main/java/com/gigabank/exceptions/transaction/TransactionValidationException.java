package com.gigabank.exceptions.transaction;

import com.gigabank.exceptions.ApiException;

/**
 * Исключение выбрасывается, если данные о транзакции не проходят валидацию.
 */
public class TransactionValidationException extends ApiException {
    public TransactionValidationException(String message) {
        super(message);
    }
}