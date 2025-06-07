package com.gigabank.models.dto.request.user;

import com.gigabank.models.dto.response.AccountResponseDto;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
public class UpdateUserRequestDto {
    @Email
    String email;
    String username;
    String phoneNumber;
    List<AccountResponseDto> listAccountDto = new ArrayList<>();
}