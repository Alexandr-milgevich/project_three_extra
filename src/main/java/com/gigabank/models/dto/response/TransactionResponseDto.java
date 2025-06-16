package com.gigabank.models.dto.response;

import com.gigabank.constants.TransactionType;
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
    TransactionType type;
    String category;
    LocalDateTime createdDate;

    // Необязательные поля — зависят от источника оплаты

    String bankName;              //Название банка (если банковский перевод).
    String cardNumber;            //Последние 4 цифры карты (если платёж по карте).
    String merchantName;          //Название магазина или поставщика услуг.
    String digitalWalletId;       //Идентификатор электронного кошелька (при оплате через электронные кошельки).
    String merchantCategoryCode;  //MCC-код продавца.

    BankAccount bankAccount;
}