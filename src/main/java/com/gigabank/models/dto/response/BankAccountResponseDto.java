package com.gigabank.models.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO для передачи данных о счете в ответах API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankAccountResponseDto {
    Long numberAccount;
    BigDecimal balance;
    String currency;
    UserResponseDto user;
    List<TransactionResponseDto> listTransactionResponseDto;
}