package com.gigabank.exceptions.handler;

import com.gigabank.exceptions.User.UserNotFoundException;
import com.gigabank.exceptions.account.AccountNotFoundException;
import com.gigabank.exceptions.account.AccountValidationException;
import com.gigabank.models.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Глобальный обработчик исключений для операций по счетам.
 * Обрабатывает исключения, связанные по счетам, и возвращает соответствующие HTTP-ответы.
 */
@Slf4j
@RestControllerAdvice
public class GlobalAccountExceptionHandler {

    /**
     * Обрабатывает исключение, когда пользователь не найден.
     *
     * @param ex исключение AccountNotFoundException
     * @return ResponseEntity с деталями ошибки и статусом 404
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFound(AccountNotFoundException ex) {
        log.warn("Account not found: {}", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    /**
     * Обрабатывает исключение при валидации данных пользователя.
     *
     * @param ex исключение AccountValidationException
     * @return ResponseEntity с деталями ошибки и статусом 400
     */
    @ExceptionHandler(AccountValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(AccountValidationException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает все остальные исключения.
     *
     * @param ex исключение Exception
     * @return ResponseEntity с деталями ошибки и статусом 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Internal error: ", ex);
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Строит объект ответа с информацией об ошибке.
     *
     * @param ex     исключение
     * @param status HTTP статус
     * @return ResponseEntity с ErrorResponse
     */
    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, HttpStatus status) {
        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(response);
    }
}