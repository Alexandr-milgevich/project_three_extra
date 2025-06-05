package com.gigabank.models.dto.request.user;

import com.gigabank.models.dto.AccountDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO для обновления данных существующего пользователя.
 * Содержит данные, которые могут быть изменены в профиле пользователя.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequestDto {
    @Email
    String email;

    String lastName;
    String firstName;
    String middleName;
    String phoneNumber;

    @Past
    LocalDate birthDate;

    List<AccountDto> listAccountDto = new ArrayList<>();
}
