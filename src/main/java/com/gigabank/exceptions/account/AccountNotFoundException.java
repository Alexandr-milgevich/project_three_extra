package com.gigabank.exceptions.account;

import com.gigabank.exceptions.ApiException;

/**
 * Исключение выбрасывается, когда не удается найти счет по идентификатору.
 */
public class AccountNotFoundException extends ApiException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}