package gigabank.accountmanagement.models.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Информация о банковском счете пользователя
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankAccountDto {
    String id;
    BigDecimal balance;
    UserDto owner;
    List<TransactionDto> transactionDtos = new ArrayList<>();
}
