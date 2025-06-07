package com.gigabank.exceptions.User;

import com.gigabank.exceptions.ApiException;

/**
 * Исключение выбрасывается, когда при создании пользователя
 * обнаруживается, что пользователь с таким email или телефоном
 * уже существует в системе.
 */
public class UserAlreadyExistsException extends ApiException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}