package com.gigabank.exceptions.account;

import com.gigabank.exceptions.ApiException;

/**
 * Исключение выбрасывается, если данные о счете не проходят валидацию.
 */
public class AccountValidationException extends ApiException {
    public AccountValidationException(String message) {
        super(message);
    }
}