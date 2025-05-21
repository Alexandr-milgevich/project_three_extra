package gigabank.accountmanagement.service.bank.service;

import gigabank.accountmanagement.constants.PaymentType;
import gigabank.accountmanagement.constants.TransactionType;
import gigabank.accountmanagement.models.dto.Account;
import gigabank.accountmanagement.models.dto.TransactionDto;
import gigabank.accountmanagement.models.dto.UserDto;
import gigabank.accountmanagement.service.bank.service.strategy.PaymentServiceStrategy;
import gigabank.accountmanagement.service.bank.service.factory.PaymentServiceStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис для управления счетами, включая создание, удаление и пополнение.
 * Также управляет обработкой транзакций и уведомлениями.
 * Использует стратегии ({@link PaymentServiceStrategy}) для обработки разных типов платежей.
 */
@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final PaymentServiceStrategyFactory paymentStrategyFactory;

    /**
     * Находит банковский счет по идентификатору.
     * В текущей реализации возвращает тестовый аккаунт.
     *
     * @param accountId идентификатор банковского счета
     * @return объект {@link Account} с указанным ID
     */
    public Account findAccountById(int accountId) {
        return createTestAccount();
    }

    /**
     * Снимает указанную сумму с банковского счета.
     *
     * @param account банковский счет для списания
     * @param amount  сумма для снятия
     */
    public void withdraw(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().subtract(amount));
    }

    /**
     * Обрабатывает платеж с использованием указанной стратегии.
     *
     * @param account  банковский счет для списания
     * @param amount   сумма платежа
     * @param strategy стратегия обработки платежа ({@link PaymentServiceStrategy})
     * @param details  дополнительные детали платежа
     */
    public void processPayment(Account account, BigDecimal amount,
                               PaymentServiceStrategy strategy,
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
    public void processCardPayment(Account account, BigDecimal amount,
                                   String cardNumber, String merchantName) {
        Map<String, String> details = new HashMap<>();
        details.put("category", "Education");
        details.put("createdDate", LocalDateTime.now().minusDays(10).toString());
        details.put("cardNumber", cardNumber);
        details.put("merchantName", merchantName);

        processPayment(account,
                amount,
                paymentStrategyFactory.getPaymentStrategy(PaymentType.CARD),
                details);
    }

    /**
     * Обрабатывает банковский перевод.
     *
     * @param account  банковский счет для списания
     * @param amount   сумма перевода
     * @param bankName название банка-получателя
     */
    public void processBankTransfer(Account account, BigDecimal amount, String bankName) {
        Map<String, String> details = new HashMap<>();
        details.put("category", "Beauty");
        details.put("createdDate", LocalDateTime.now().minusDays(8).toString());
        details.put("bankName", bankName);

        processPayment(account,
                amount,
                paymentStrategyFactory.getPaymentStrategy(PaymentType.BANK),
                details);
    }

    /**
     * Обрабатывает платеж через электронный кошелек.
     *
     * @param account  банковский счет для списания
     * @param amount   сумма платежа
     * @param walletId идентификатор электронного кошелька
     */
    public void processWalletPayment(Account account, BigDecimal amount, String walletId) {
        Map<String, String> details = new HashMap<>();
        details.put("category", "Health");
        details.put("createdDate", LocalDateTime.now().minusDays(6).toString());
        details.put("walletId", walletId);

        processPayment(account,
                amount,
                paymentStrategyFactory.getPaymentStrategy(PaymentType.WALLET),
                details);
    }

    public gigabank.accountmanagement.models.entity.Account create() {
        gigabank.accountmanagement.models.entity.Account account = new gigabank.accountmanagement.models.entity.Account();
        return null;
    }

    /**
     * Создает тестовый банковский счет с демонстрационными данными.
     *
     * @return объект {@link Account} с тестовыми данными
     */
    public static Account createTestAccount() {
        UserDto testUserDto = new UserDto();
        testUserDto.setId("user123");
        testUserDto.setFirstName("John");
        testUserDto.setMiddleName("K");
        testUserDto.setLastName("Doe");
        testUserDto.setBirthDate(LocalDateTime.now().minusYears(25).toLocalDate());
        testUserDto.setEmail("john.doe@example.com");
        testUserDto.setPhoneNumber("+1234567890");

        Account account = new Account();
        account.setId("acc123");
        account.setBalance(new BigDecimal("5000.00"));
        account.setOwner(testUserDto);

        TransactionDto transactionDto1 = TransactionDto.builder()
                .id("tx001")
                .amount(new BigDecimal("100.00"))
                .type(TransactionType.PAYMENT)
                .category("Electronics")
                .createdDate(LocalDateTime.now().minusDays(5))
                .build();

        TransactionDto transactionDto2 = TransactionDto.builder()
                .id("tx002")
                .amount(new BigDecimal("200.00"))
                .type(TransactionType.DEPOSIT)
                .category("Groceries")
                .createdDate(LocalDateTime.now().minusDays(2))
                .build();

        account.getTransactionDto().addAll(List.of(transactionDto1, transactionDto2));

        return account;
    }
}