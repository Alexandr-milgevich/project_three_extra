package com.gigabank.models.dto.request.user;

import com.gigabank.constants.PaymentType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Используется для передачи данных между клиентом и сервером при выполнении финансовых операций.
 * Содержит информацию о счете, сумме операции, типе платежа и дополнительных деталях платежа.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAnotherRequestDto {
    Long accountId;
    BigDecimal amount;
    PaymentType paymentType;
    Map<String, String> paymentDetails;
}
