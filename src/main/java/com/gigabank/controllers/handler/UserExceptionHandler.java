package com.gigabank.controllers.handler;

import com.gigabank.exceptions.User.UserAlreadyExistsException;
import com.gigabank.exceptions.User.UserNotFoundException;
import com.gigabank.exceptions.User.UserValidationException;
import com.gigabank.models.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.gigabank.utility.ErrorResponseUtil.buildErrorResponse;

/**
 * Глобальный обработчик исключений, связанных с операциями над пользователями.
 * Перехватывает пользовательские исключения и возвращает корректные HTTP ответы.
 */
@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {

    /**
     * Обработка ошибок валидации данных пользователя.
     *
     * @param ex исключение UserValidationException
     * @return HTTP 400 с описанием ошибки
     */
    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(UserValidationException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработка ошибки, когда пользователь не найден.
     *
     * @param ex исключение UserNotFoundException
     * @return HTTP 404 с описанием ошибки
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        log.warn("User not found: {}", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    /**
     * Обработка ошибки, когда пользователь уже существует.
     *
     * @param ex исключение UserAlreadyExistsException
     * @return HTTP 409 с описанием ошибки
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserExists(UserAlreadyExistsException ex) {
        log.warn("User conflict: {}", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.CONFLICT);
    }
}