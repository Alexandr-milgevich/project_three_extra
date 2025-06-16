package com.gigabank.models.dto.request.user;

import com.gigabank.models.dto.response.AccountResponseDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
public class UpdateUserRequestDto {
    Long id;
    String username;
    @Email
    String email;
    @Pattern(regexp = "\\+?\\d{10,20}", message = "Invalid phone number format")
    String phoneNumber;
    List<AccountResponseDto> listAccountDto;
}