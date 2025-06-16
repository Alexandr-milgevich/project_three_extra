package com.gigabank.controllers.handler;

import com.gigabank.exceptions.buisnes_logic.EntityExistsException;
import com.gigabank.exceptions.buisnes_logic.EntityNotFoundException;
import com.gigabank.exceptions.buisnes_logic.EntityValidationException;
import com.gigabank.models.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.gigabank.utility.ErrorResponseUtil.buildErrorResponse;

/**
 * Глобальный обработчик типовых исключений, связанных с бизнес-логикой.
 * Перехватывает стандартные RuntimeException и иные распространённые ошибки.
 */
@Slf4j
@RestControllerAdvice
public class BusinessExceptionHandler {

    /**
     * Обработка ошибок валидации данных.
     *
     * @param ex исключение EntityValidationException
     * @return HTTP 400 с описанием ошибки
     */
    @ExceptionHandler(EntityValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(EntityValidationException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработка ошибки, когда сущность не найдена.
     *
     * @param ex исключение EntityNotFoundException
     * @return HTTP 404 с описанием ошибки
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        log.warn("Entity not found: {}", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    /**
     * Обработка ошибки, когда объект уже существует.
     *
     * @param ex исключение EntityExistsException
     * @return HTTP 409 с описанием ошибки
     */
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserExists(EntityExistsException ex) {
        log.warn("Entity conflict: {}", ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.CONFLICT);
    }
}