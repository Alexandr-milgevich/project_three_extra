package com.gigabank.models.dto.request.account;

import com.gigabank.models.dto.response.UserResponseDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * DTO для запроса создания счета.
 * Содержит минимальные данные для открытия нового счета.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateBankAccountRequestDto {
    UserResponseDto userResponseDto;
}