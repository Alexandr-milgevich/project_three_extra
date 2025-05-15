package gigabank.accountmanagement.service.factory;

import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Фабрика для создания экземпляров {@link Transaction} с преднастроенными параметрами.
 * Предоставляет удобные методы для различных типов транзакций.
 */
public class TransactionFactory {

    /**
     * Вспомогательный метод для создания стандартной части транзакции.
     *
     * @param id          уникальный идентификатор транзакции
     * @param amount      сумма
     * @param type        тип транзакции
     * @param category    категория
     * @param createdDate дата создания
     * @return готовый билдера для транзакции с общими параметрами
     */
    private static Transaction.TransactionBuilder createBaseTransaction(String id, BigDecimal amount, TransactionType type, String category, LocalDateTime createdDate) {
        return Transaction.hiddenBuilder()
                .id(id)
                .amount(amount)
                .type(type)
                .category(category)
                .createdDate(createdDate);
    }

    /**
     * Создает транзакцию для оплаты по карте.
     *
     * @param id          уникальный идентификатор транзакции
     * @param amount      сумма
     * @param type        тип транзакции
     * @param category    категория
     * @param createdDate дата создания
     * @param cardNumber  последние 4 цифры карты
     * @return готовый экземпляр транзакции для оплаты по карте
     */
    public static Transaction createCardPayment(String id, BigDecimal amount, TransactionType type, String category, LocalDateTime createdDate, String cardNumber) {
        return createBaseTransaction(id, amount, type, category, createdDate)
                .cardNumber(cardNumber)
                .build();
    }

    /**
     * Создает транзакцию для банковского перевода.
     *
     * @param id          уникальный идентификатор транзакции
     * @param amount      сумма
     * @param type        тип транзакции
     * @param category    категория
     * @param createdDate дата создания
     * @param bankName    название банка
     * @return готовый экземпляр транзакции для банковского перевода
     */
    public static Transaction createBankTransfer(String id, BigDecimal amount, TransactionType type, String category, LocalDateTime createdDate, String bankName) {
        return createBaseTransaction(id, amount, type, category, createdDate)
                .bankName(bankName)
                .build();
    }

    /**
     * Создает транзакцию для оплаты через электронный кошелек.
     *
     * @param id          уникальный идентификатор транзакции
     * @param amount      сумма
     * @param type        тип транзакции
     * @param category    категория
     * @param createdDate дата создания
     * @param walletId    ID электронного кошелька
     * @return готовый экземпляр транзакции для оплаты электронный кошелек
     */
    public static Transaction createWalletPayment(String id, BigDecimal amount, TransactionType type, String category, LocalDateTime createdDate, String walletId) {
        return createBaseTransaction(id, amount, type, category, createdDate)
                .digitalWalletId(walletId)
                .build();
    }

    /**
     * Создает универсальную транзакцию для произвольного использования.
     *
     * @param id          уникальный идентификатор транзакции
     * @param amount      сумма
     * @param type        тип транзакции
     * @param category    категория
     * @param createdDate дата создания
     * @return готовый экземпляр транзакции для базового использования
     */
    public static Transaction createGeneric(String id, BigDecimal amount, TransactionType type, String category, LocalDateTime createdDate) {
        return createBaseTransaction(id, amount, type, category, createdDate)
                .build();
    }
}