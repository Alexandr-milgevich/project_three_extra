package com.gigabank.exceptions.transaction;

import com.gigabank.exceptions.ApiException;

/**
 * Исключение выбрасывается, когда не удается найти транзакцию по идентификатору.
 */
public class TransactionNotFoundException extends ApiException {
    public TransactionNotFoundException(String message) {
        super(message);
    }
}