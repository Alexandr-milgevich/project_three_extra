package com.gigabank.models.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * DTO для представления пользователя в ответах API.
 * Содержит все данные о пользователе, которые могут быть возвращены клиенту.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDto {
    String email;
    String username;
    String phoneNumber;
    List<AccountResponseDto> listAccountDto;
}