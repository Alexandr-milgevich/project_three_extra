package com.gigabank.models.dto.request.user;

import com.gigabank.models.dto.response.BankAccountResponseDto;
import jakarta.validation.constraints.Email;
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
    String phoneNumber;
    List<BankAccountResponseDto> listAccountDto;
}