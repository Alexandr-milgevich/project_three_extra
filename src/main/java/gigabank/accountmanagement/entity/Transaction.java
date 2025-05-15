package gigabank.accountmanagement.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность, описывающая банковскую транзакцию.
 * Используется для хранения информации о платежах, переводах и других операциях.
 */
@Getter
@Builder(builderMethodName = "hiddenBuilder")
public class Transaction {
    @Builder.Default
    private String id = UUID.randomUUID().toString(); //Уникальный идентификатор транзакции (генерируется автоматически).
    @NonNull
    private BigDecimal amount;  //Сумма транзакции.
    @Builder.Default
    private TransactionType type = TransactionType.PAYMENT; //Тип транзакции (DEPOSIT, WITHDRAWAL, PAYMENT).
    @NonNull
    private String category;    //Категория транзакции.
    @NonNull
    private LocalDateTime createdDate;  //Дата и время создания транзакции.

    // Необязательные поля — зависят от источника оплаты

    private String merchantName;          //Название магазина или поставщика услуг.
    private String merchantCategoryCode;  //MCC-код продавца.
    private String cardNumber;            //Последние 4 цифры карты (если платёж по карте).
    private String bankName;              //Название банка (если банковский перевод).
    private String digitalWalletId;       //Идентификатор электронного кошелька (при оплате через электронные кошельки).
}