package com.gigabank.controllers.handler;

import com.gigabank.models.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.gigabank.utility.ErrorResponseUtil.buildErrorResponse;

/**
 * Глобальный обработчик типовых исключений, не связанных с бизнес-логикой.
 * Перехватывает стандартные RuntimeException и иные распространённые ошибки.
 */
@Slf4j
@RestControllerAdvice
public class GenericExceptionHandler {

    /**
     * Обработка IllegalArgumentException — некорректные аргументы.
     *
     * @param ex исключение IllegalArgumentException
     * @return HTTP 400 с описанием ошибки
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Некорректный аргумент: {}", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработка IllegalStateException — вызов в неподходящем состоянии.
     *
     * @param ex исключение IllegalStateException
     * @return HTTP 409 с описанием ошибки
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        log.error("Неверное состояние приложения: ", ex);
        return buildErrorResponse(ex, HttpStatus.CONFLICT);
    }

    /**
     * Обработка NullPointerException — ошибка в коде (null).
     *
     * @param ex исключение NullPointerException
     * @return HTTP 500 с описанием ошибки
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        log.error("Ошибка NullPointerException: ", ex);
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Обработка прочих исключений, не перехваченных выше.
     *
     * @param ex любое исключение
     * @return HTTP 500 с описанием ошибки
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Внутренняя ошибка сервера: ", ex);
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}