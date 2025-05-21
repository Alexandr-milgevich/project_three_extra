package gigabank.accountmanagement.models.dto;

import gigabank.accountmanagement.constants.TransactionType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
public class TransactionDto {
    @Builder.Default
    String id = UUID.randomUUID().toString();   //Уникальный идентификатор транзакции (генерируется автоматически).
    @NonNull
    BigDecimal amount;  //Сумма транзакции.
    @Builder.Default
    TransactionType type = TransactionType.PAYMENT; //Тип транзакции (DEPOSIT, WITHDRAWAL, PAYMENT).
    @NonNull
    String category;    //Категория транзакции.
    @NonNull
    LocalDateTime createdDate;  //Дата и время создания транзакции.

    // Необязательные поля — зависят от источника оплаты

    private String bankName;              //Название банка (если банковский перевод).
    private String cardNumber;            //Последние 4 цифры карты (если платёж по карте).
    private String merchantName;          //Название магазина или поставщика услуг.
    private String digitalWalletId;       //Идентификатор электронного кошелька (при оплате через электронные кошельки).
    private String merchantCategoryCode;  //MCC-код продавца.
}