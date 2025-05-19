package gigabank.accountmanagement.models.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

/**
 * Возврат по транзакции
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefundDto {
    int id;
    BigDecimal amount;
    String description;
    int transactionId;
}
