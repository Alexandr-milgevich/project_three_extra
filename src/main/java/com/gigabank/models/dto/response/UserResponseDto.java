package com.gigabank.models.dto.response;

import com.gigabank.models.dto.AccountDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
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
    Long id;
    String email;
    String lastName;
    String firstName;
    String middleName;
    String phoneNumber;
    LocalDate birthDate;
    List<AccountDto> accounts;
}