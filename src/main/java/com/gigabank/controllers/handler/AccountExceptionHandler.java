package com.gigabank.controllers.handler;

import com.gigabank.exceptions.account.AccountNotFoundException;
import com.gigabank.exceptions.account.AccountValidationException;
import com.gigabank.models.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.gigabank.utility.ErrorResponseUtil.buildErrorResponse;

/**
 * Глобальный обработчик исключений, связанных с операциями по счетам.
 * Перехватывает исключения по счетам и формирует корректные HTTP ответы.
 */
@Slf4j
@RestControllerAdvice
public class AccountExceptionHandler {

    /**
     * Обработка ошибок валидации данных счета.
     *
     * @param ex исключение AccountValidationException
     * @return HTTP 400 с описанием ошибки
     */
    @ExceptionHandler(AccountValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(AccountValidationException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработка ошибки, когда счет не найден.
     *
     * @param ex исключение AccountNotFoundException
     * @return HTTP 404 с описанием ошибки
     */
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFound(AccountNotFoundException ex) {
        log.warn("Account not found: {}", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }
}