package com.gigabank.models.dto.request.user;

import com.gigabank.models.dto.response.AccountResponseDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO для запроса по работе с пользователем.
 * Содержит данные для совершения операций с пользователем.
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

    @NotBlank(message = "Не указано имя пользователя")
    String username;

    @NotBlank(message = "Не указан номер телефона")
    String phoneNumber;

    @NotNull(message = "У пользователя должен быть счет")
    List<AccountResponseDto> listAccountDto = new ArrayList<>();
}