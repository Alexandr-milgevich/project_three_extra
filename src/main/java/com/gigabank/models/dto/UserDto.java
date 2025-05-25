package com.gigabank.models.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Информация о пользователе
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @NotNull
    @PositiveOrZero
    Long id;

    @NotBlank(message = "Не указана эл. почта")
    @Email(message = "email должен быть корректным")
    String email;

    @NotBlank(message = "Не указана фамилия")
    String lastName;

    @NotBlank(message = "Не указано имя")
    String firstName;

    @NotBlank(message = "Не указан номер телефона")
    String phoneNumber;

    @NotBlank(message = "Дата рождения не указана.")
    @PastOrPresent(message = "Дата рождения не может быть в будущем.")
    LocalDate birthDate;

    String middleName;

    List<AccountDto> listAccountDto = new ArrayList<>();
}
