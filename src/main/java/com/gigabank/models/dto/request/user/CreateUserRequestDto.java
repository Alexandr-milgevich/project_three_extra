package com.gigabank.models.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO для создания нового пользователя.
 * Содержит обязательные данные для регистрации нового пользователя.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequestDto {
    @NotBlank(message = "Не указано имя пользователя")
    String username;

    @NotBlank(message = "Не указана эл. почта")
    @Email(message = "email должен быть корректным")
    String email;

    @NotBlank(message = "Не указан номер телефона")
    String phoneNumber;
}