package com.gigabank.models.dto.request.transaction;

import com.gigabank.constants.TransactionType;
import com.gigabank.models.entity.BankAccount;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO для запроса по работе с транзакцией.
 * Содержит данные для совершения операций с транзакцией.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionRequestDto {
    @NotNull(message = "Транзакция не может быть пустой")
    @PositiveOrZero
    Long id;

    @NotBlank(message = "Транзакция не может быть пустой")
    String transactionUuid;

    @NotNull(message = "Сумма обязательна")
    @PositiveOrZero(message = "Сумма транзакции не может быть отрицательной")
    BigDecimal amount;

    @NotNull
    @Builder.Default
    String type = TransactionType.PAYMENT.name(); //Тип транзакции (DEPOSIT, WITHDRAWAL, PAYMENT).

    @NotBlank(message = "Категория транзакции должна быть указана")
    String category;

    @NotEmpty(message = "Дата и время создания транзакции должна быть указана")
    @PastOrPresent(message = "Дата и время создания транзакции не может быть в будущем.")
    LocalDateTime createdDate;

    // Необязательные поля — зависят от источника оплаты

    String bankName;              //Название банка (если банковский перевод).
    String cardNumber;            //Последние 4 цифры карты (если платёж по карте).
    String merchantName;          //Название магазина или поставщика услуг.
    String digitalWalletId;       //Идентификатор электронного кошелька (при оплате через электронные кошельки).
    String merchantCategoryCode;  //MCC-код продавца.

    @NotNull(message = "Счет не может быть пустым")
    @NotEmpty(message = "Транзакция должна быть привязана к счету")
    BankAccount bankAccount;
}