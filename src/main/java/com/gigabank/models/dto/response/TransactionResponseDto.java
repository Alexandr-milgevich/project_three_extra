package com.gigabank.models.dto.response;

import com.gigabank.models.entity.BankAccount;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Сущность, описывающая банковскую транзакцию.
 * Используется для хранения информации о платежах, переводах и других операциях.
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TransactionResponseDto {
    String transactionUuid;
    BigDecimal amount;
    String type;
    LocalDateTime createdDate;
    BankAccount bankAccount;
}