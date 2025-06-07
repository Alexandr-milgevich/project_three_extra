package com.gigabank.utility;

import com.gigabank.models.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

/**
 * Класс для формирования HTTP-ответов с информацией об ошибках.
 * Используется глобальными обработчиками исключений для единообразного построения ответов.
 */
public class ErrorResponseUtil {
    /**
     * Формирует ResponseEntity с объектом ErrorResponse, содержащим детали ошибки.
     */
    public static ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, HttpStatus status) {
        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(response);
    }
}