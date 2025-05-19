package gigabank.accountmanagement.service;

import gigabank.accountmanagement.constants.TransactionType;
import gigabank.accountmanagement.models.dto.BankAccount;
import gigabank.accountmanagement.models.dto.Transaction;
import gigabank.accountmanagement.models.dto.User;
import gigabank.accountmanagement.design.strategy.payment.bank.service.BankTransferStrategy;
import gigabank.accountmanagement.design.strategy.payment.bank.service.CardPaymentStrategy;
import gigabank.accountmanagement.design.strategy.payment.bank.service.DigitalWalletStrategy;
import gigabank.accountmanagement.design.strategy.payment.bank.service.PaymentStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис для управления счетами, включая создание, удаление и пополнение.
 * Также управляет обработкой транзакций и уведомлениями.
 * Использует стратегии ({@link PaymentStrategy}) для обработки разных типов платежей.
 */
@Service
public class BankAccountService {

    /**
     * Находит банковский счет по идентификатору.
     * В текущей реализации возвращает тестовый аккаунт.
     *
     * @param accountId идентификатор банковского счета
     * @return объект {@link BankAccount} с указанным ID
     */
    public BankAccount findAccountById(int accountId) {
        return createTestAccount();
    }

    /**
     * Снимает указанную сумму с банковского счета.
     *
     * @param account банковский счет для списания
     * @param amount  сумма для снятия
     */
    public void withdraw(BankAccount account, BigDecimal amount) {
        account.setBalance(account.getBalance().subtract(amount));
    }

    /**
     * Обрабатывает платеж с использованием указанной стратегии.
     *
     * @param account  банковский счет для списания
     * @param amount   сумма платежа
     * @param strategy стратегия обработки платежа ({@link PaymentStrategy})
     * @param details  дополнительные детали платежа
     */
    public void processPayment(BankAccount account, BigDecimal amount,
                               PaymentStrategy strategy,
                               Map<String, String> details) {
        strategy.process(account, amount, details);
    }

    /**
     * Обрабатывает платеж банковской картой.
     *
     * @param account      банковский счет для списания
     * @param amount       сумма платежа
     * @param cardNumber   последние 4 цифры номера карты
     * @param merchantName название мерчанта
     */
    public void processCardPayment(BankAccount account, BigDecimal amount,
                                   String cardNumber, String merchantName) {
        Map<String, String> details = new HashMap<>();
        details.put("category", "Education");
        details.put("createdDate", LocalDateTime.now().minusDays(10).toString());
        details.put("cardNumber", cardNumber);
        details.put("merchantName", merchantName);

        processPayment(account, amount, new CardPaymentStrategy(), details);
    }

    /**
     * Обрабатывает банковский перевод.
     *
     * @param account  банковский счет для списания
     * @param amount   сумма перевода
     * @param bankName название банка-получателя
     */
    public void processBankTransfer(BankAccount account, BigDecimal amount, String bankName) {
        Map<String, String> details = new HashMap<>();
        details.put("category", "Beauty");
        details.put("createdDate", LocalDateTime.now().minusDays(8).toString());
        details.put("bankName", bankName);

        processPayment(account, amount, new BankTransferStrategy(), details);
    }

    /**
     * Обрабатывает платеж через электронный кошелек.
     *
     * @param account  банковский счет для списания
     * @param amount   сумма платежа
     * @param walletId идентификатор электронного кошелька
     */
    public void processWalletPayment(BankAccount account, BigDecimal amount, String walletId) {
        Map<String, String> details = new HashMap<>();
        details.put("category", "Health");
        details.put("createdDate", LocalDateTime.now().minusDays(6).toString());
        details.put("walletId", walletId);

        processPayment(account, amount, new DigitalWalletStrategy(), details);
    }

    /**
     * Создает тестовый банковский счет с демонстрационными данными.
     *
     * @return объект {@link BankAccount} с тестовыми данными
     */
    public static BankAccount createTestAccount() {
        User testUser = new User();
        testUser.setId("user123");
        testUser.setFirstName("John");
        testUser.setMiddleName("K");
        testUser.setLastName("Doe");
        testUser.setBirthDate(LocalDateTime.now().minusYears(25).toLocalDate());
        testUser.setEmail("john.doe@example.com");
        testUser.setPhoneNumber("+1234567890");

        BankAccount account = new BankAccount();
        account.setId("acc123");
        account.setBalance(new BigDecimal("5000.00"));
        account.setOwner(testUser);

        Transaction transaction1 = Transaction.hiddenBuilder()
                .id("tx001")
                .amount(new BigDecimal("100.00"))
                .type(TransactionType.PAYMENT)
                .category("Electronics")
                .createdDate(LocalDateTime.now().minusDays(5))
                .build();

        Transaction transaction2 = Transaction.hiddenBuilder()
                .id("tx002")
                .amount(new BigDecimal("200.00"))
                .type(TransactionType.DEPOSIT)
                .category("Groceries")
                .createdDate(LocalDateTime.now().minusDays(2))
                .build();

        account.getTransactions().addAll(List.of(transaction1, transaction2));

        return account;
    }
}