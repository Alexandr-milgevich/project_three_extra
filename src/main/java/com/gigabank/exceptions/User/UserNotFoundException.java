package com.gigabank.exceptions.User;

import com.gigabank.exceptions.ApiException;

/**
 * Исключение выбрасывается, когда не удается найти пользователя
 * по предоставленному идентификатору или email.
 */
public class UserNotFoundException extends ApiException {
    public UserNotFoundException(String message) {
        super(message);
    }
}