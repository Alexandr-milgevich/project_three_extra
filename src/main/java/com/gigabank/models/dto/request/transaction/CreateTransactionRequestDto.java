package com.gigabank.models.dto.request.transaction;

import com.gigabank.constants.TransactionType;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO для запроса создания транзакции.
 * Содержит минимальные данные для создания новой транзакции.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTransactionRequestDto {

    @NotNull(message = "Сумма обязательна")
    @PositiveOrZero(message = "Сумма транзакции не может быть отрицательной")
    BigDecimal amount;

    @Builder.Default
    String type = TransactionType.PAYMENT.name();

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
}