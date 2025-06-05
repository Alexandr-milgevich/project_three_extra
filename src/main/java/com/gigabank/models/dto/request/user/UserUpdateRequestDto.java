package com.gigabank.models.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO для обновления данных существующего пользователя.
 * Содержит данные, которые могут быть изменены в профиле пользователя.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {
    @Email
    private String email;

    private String lastName;
    private String firstName;
    private String middleName;
    private String phoneNumber;

    @Past
    private LocalDate birthDate;
}
