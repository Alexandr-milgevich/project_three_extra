package gigabank.accountmanagement.models.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Информация о банковском счете пользователя
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {
    String id = UUID.randomUUID().toString();
    BigDecimal balance;
    UserDto owner;
    List<TransactionDto> transactionDto = new ArrayList<>();
}
