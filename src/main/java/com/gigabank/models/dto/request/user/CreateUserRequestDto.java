package com.gigabank.models.dto.request.user;

import com.gigabank.models.dto.response.AccountResponseDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
    @Pattern(regexp = "\\+?\\d{10,20}", message = "Invalid phone number format")
    String phoneNumber;

    List<AccountResponseDto> listAccountDto;
}