package gigabank.accountmanagement.models.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequestDto {
    int accountId;
    BigDecimal amount;
    String paymentType;
    Map<String, String> paymentDetails;
}
