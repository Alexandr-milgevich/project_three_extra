package com.gigabank.models.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO для передачи данных о счете в ответах API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponseDto {
    Long id;
    BigDecimal balance;
    UserResponseDto userResponseDto;
    List<TransactionResponseDto> listTransactionResponseDto = new ArrayList<>();
}