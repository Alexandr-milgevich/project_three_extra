package gigabank.accountmanagement.models.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Информация о пользователе
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    String id = UUID.randomUUID().toString();
    String email;
    String lastName;
    String firstName;
    String middleName;
    String phoneNumber;
    LocalDate birthDate;
    List<Account> accounts = new ArrayList<>();
}
