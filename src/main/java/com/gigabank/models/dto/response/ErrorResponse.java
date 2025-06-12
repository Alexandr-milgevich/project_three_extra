package com.gigabank.models.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * Ответ с ошибкой, возвращаемый в случае возникновения исключений.
 * Содержит информацию о времени ошибки, статусе, сообщении и ошибке.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {
    Integer status;             //Статус ошибки.
    String error;               //Тип ошибки.
    String message;             //Сообщение об ошибке.
    LocalDateTime timestamp;    //Время, когда произошла ошибка.
}
