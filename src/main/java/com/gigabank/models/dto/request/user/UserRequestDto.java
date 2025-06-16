package com.gigabank.models.dto.request.user;

import com.gigabank.constants.status.UserStatus;
import com.gigabank.models.dto.response.AccountResponseDto;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    @NotNull
    @PositiveOrZero
    Long id;

    @NotBlank(message = "Не указана эл. почта")
    @Email(message = "email должен быть корректным")
    String email;

    @NotBlank(message = "Не указано имя пользователя")
    String username;

    @NotBlank(message = "Не указан номер телефона")
    String phoneNumber;

    @NotNull
    @NotEmpty(message = "Статус не может быть пустым")
    UserStatus status;

    @NotNull
    @NotEmpty(message = "У пользователя должны быть счета")
    List<AccountResponseDto> listAccountDto;
}