package com.gigabank.models.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * DTO для создания нового пользователя.
 * Содержит обязательные данные для регистрации нового пользователя.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequestDto {
    @NotBlank(message = "Не указана эл. почта")
    @Email(message = "email должен быть корректным")
    String email;

    @NotBlank(message = "Не указана фамилия")
    String lastName;

    @NotBlank(message = "Не указано имя")
    String firstName;

    String middleName;

    @NotBlank(message = "Не указан номер телефона")
    String phoneNumber;

    @NotNull(message = "Дата рождения не указана.")
    @PastOrPresent(message = "Дата рождения не может быть в будущем.")
    LocalDate birthDate;
}