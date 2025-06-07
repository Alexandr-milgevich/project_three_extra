package com.gigabank.controllers.handler;

import com.gigabank.exceptions.transaction.TransactionNotFoundException;
import com.gigabank.exceptions.transaction.TransactionValidationException;
import com.gigabank.models.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.gigabank.utility.ErrorResponseUtil.buildErrorResponse;

/**
 * Глобальный обработчик исключений, связанных с операциями по транзакциям.
 * Перехватывает исключения транзакций и формирует корректные HTTP ответы.
 */
@Slf4j
@RestControllerAdvice
public class TransactionExceptionHandler {

    /**
     * Обработка ошибок валидации данных транзакции.
     *
     * @param ex исключение TransactionValidationException
     * @return HTTP 400 с описанием ошибки
     */
    @ExceptionHandler(TransactionValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(TransactionValidationException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработка ошибки, когда транзакция не найдена.
     *
     * @param ex исключение TransactionNotFoundException
     * @return HTTP 404 с описанием ошибки
     */
    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTransactionNotFound(TransactionNotFoundException ex) {
        log.warn("Transaction not found: {}", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }
}