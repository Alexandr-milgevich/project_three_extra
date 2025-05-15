package gigabank.accountmanagement.service;

import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.Transaction;
import gigabank.accountmanagement.entity.TransactionType;
import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.service.strategy.payment.bank.service.BankTransferStrategy;
import gigabank.accountmanagement.service.strategy.payment.bank.service.CardPaymentStrategy;
import gigabank.accountmanagement.service.strategy.payment.bank.service.DigitalWalletStrategy;
import gigabank.accountmanagement.service.strategy.payment.bank.service.PaymentStrategy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис для управления счетами, включая создание, удаление и пополнение.
 * Также управляет обработкой транзакций и уведомлениями.
 */
public class BankAccountService {
    /**
     * Ищет банковский аккаунт по ID.
     *
     * @param accountId Идентификатор аккаунта.
     * @return {@link BankAccount} с указанным ID.
     */
    public BankAccount findAccountById(int accountId) {
        return createTestAccount();
    }

    /**
     * Снимает деньги с указанного банковского аккаунта.
     *
     * @param account Аккаунт, с которого будут сняты деньги.
     * @param amount  Сумма, которую необходимо снять.
     */
    public void withdraw(BankAccount account, BigDecimal amount) {
        account.setBalance(account.getBalance().subtract(amount));
    }

    public void processPayment(BankAccount account, BigDecimal amount,
                               PaymentStrategy strategy,
                               Map<String, String> details) {
        strategy.process(account, amount, details);
    }

    /**
     * Обрабатывает платеж картой для указанного аккаунта.
     *
     * @param account    Аккаунт, с которого будет списана сумма.
     * @param amount     Сумма платежа.
     * @param cardNumber Номер карты (последние 4 цифры).
     */
    public void processCardPayment(BankAccount account, BigDecimal amount, String cardNumber, String merchantName) {
        Map<String, String> details = new HashMap<>();
        details.put("category", "Education");
        details.put("createdDate", LocalDateTime.now().minusDays(10).toString());
        details.put("cardNumber", cardNumber);
        details.put("merchantName", merchantName);

        processPayment(account, amount, new CardPaymentStrategy(), details);
    }

    /**
     * Обрабатывает банковский перевод для указанного аккаунта.
     *
     * @param account  Аккаунт, с которого будет списана сумма.
     * @param amount   Сумма перевода.
     * @param bankName Название банка.
     */
    public void processBankTransfer(BankAccount account, BigDecimal amount, String bankName) {
        Map<String, String> details = new HashMap<>();
        details.put("category", "Beauty");
        details.put("createdDate", LocalDateTime.now().minusDays(8).toString());
        details.put("bankName", bankName);

        processPayment(account, amount, new BankTransferStrategy(), details);
    }

    /**
     * Обрабатывает платеж через электронный кошелек для указанного аккаунта.
     *
     * @param account  Аккаунт, с которого будет списана сумма.
     * @param amount   Сумма платежа.
     * @param walletId Идентификатор электронного кошелька.
     */
    public void processWalletPayment(BankAccount account, BigDecimal amount, String walletId) {
        Map<String, String> details = new HashMap<>();
        details.put("category", "Health");
        details.put("createdDate", LocalDateTime.now().minusDays(6).toString());
        details.put("walletId", walletId);

        processPayment(account, amount, new DigitalWalletStrategy(), details);
    }

    /**
     * Создает тестовый банковский аккаунт с тестовыми данными.
     *
     * @return {@link BankAccount} с тестовыми данными.
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
