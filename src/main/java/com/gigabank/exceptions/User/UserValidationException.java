package com.gigabank.exceptions.User;

import com.gigabank.exceptions.ApiException;

/**
 * Исключение выбрасывается, если данные пользователя не проходят
 * валидацию (например, неправильный формат email или недопустимая дата рождения).
 */
public class UserValidationException extends ApiException {
  public UserValidationException(String message) {
    super(message);
  }
}
