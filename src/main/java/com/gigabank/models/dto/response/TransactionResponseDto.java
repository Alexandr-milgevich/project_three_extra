package com.gigabank.models.dto.response;

import com.gigabank.constants.TransactionType;
import jakarta.validation.constraints.*;
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
    @NotNull
    @PositiveOrZero
    Long id;

    @NotNull(message = "Сумма обязательна")
    @PositiveOrZero(message = "Сумма транзакции не может быть отрицательной")
    BigDecimal amount;

    @Builder.Default
    TransactionType type = TransactionType.PAYMENT; //Тип транзакции (DEPOSIT, WITHDRAWAL, PAYMENT).

    @NotBlank(message = "Категория транзакции должна быть указана")
    String category;

    @NotEmpty(message = "Дата и время создания транзакции должна быть указана")
    @PastOrPresent(message = "Дата и время создания транзакции не может быть в будущем.")
    LocalDateTime createdDate;

    // Необязательные поля — зависят от источника оплаты

    private String bankName;              //Название банка (если банковский перевод).
    private String cardNumber;            //Последние 4 цифры карты (если платёж по карте).
    private String merchantName;          //Название магазина или поставщика услуг.
    private String digitalWalletId;       //Идентификатор электронного кошелька (при оплате через электронные кошельки).
    private String merchantCategoryCode;  //MCC-код продавца.
}