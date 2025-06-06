package com.gigabank.models.dto.request.account;

import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.models.dto.response.UserResponseDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO для запроса создания счета.
 * Содержит минимальные данные для открытия нового счета.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateAccountRequestDto {
    BigDecimal balance;
    UserResponseDto userResponseDto;
    List<TransactionResponseDto> listTransactionResponseDto = new ArrayList<>();
}