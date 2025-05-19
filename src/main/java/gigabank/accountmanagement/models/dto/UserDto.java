package gigabank.accountmanagement.models.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Информация о пользователе
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    String id;
    String email;
    String lastName;
    String firstName;
    String middleName;
    String phoneNumber;
    LocalDate birthDate;
    List<BankAccountDto> bankAccountDtos = new ArrayList<>();
}
