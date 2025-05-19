package gigabank.accountmanagement.service;

import gigabank.accountmanagement.constants.TransactionType;
import gigabank.accountmanagement.models.dto.BankAccountDto;
import gigabank.accountmanagement.models.dto.TransactionDto;
import gigabank.accountmanagement.models.dto.UserDto;
import gigabank.accountmanagement.service.design.strategy.payment.bank.service.BankTransferStrategy;
import gigabank.accountmanagement.service.design.strategy.payment.bank.service.CardPaymentStrategy;
import gigabank.accountmanagement.service.design.strategy.payment.bank.service.DigitalWalletStrategy;
import gigabank.accountmanagement.service.design.strategy.payment.bank.service.PaymentStrategy;
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
 * Использует стратегии ({@link PaymentStrategy}) для обработки разных типов платежей.
 */
@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankTransferStrategy bankTransferStrategy;
    private final CardPaymentStrategy cardPaymentStrategy;
    private final DigitalWalletStrategy digitalWalletStrategy;

    /**
     * Находит банковский счет по идентификатору.
     * В текущей реализации возвращает тестовый аккаунт.
     *
     * @param accountId идентификатор банковского счета
     * @return объект {@link BankAccountDto} с указанным ID
     */
    public BankAccountDto findAccountById(int accountId) {
        return createTestAccount();
    }

    /**
     * Снимает указанную сумму с банковского счета.
     *
     * @param account банковский счет для списания
     * @param amount  сумма для снятия
     */
    public void withdraw(BankAccountDto account, BigDecimal amount) {
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
    public void processPayment(BankAccountDto account, BigDecimal amount,
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
    public void processCardPayment(BankAccountDto account, BigDecimal amount,
                                   String cardNumber, String merchantName) {
        Map<String, String> details = new HashMap<>();
        details.put("category", "Education");
        details.put("createdDate", LocalDateTime.now().minusDays(10).toString());
        details.put("cardNumber", cardNumber);
        details.put("merchantName", merchantName);

        processPayment(account, amount, cardPaymentStrategy, details);
    }

    /**
     * Обрабатывает банковский перевод.
     *
     * @param account  банковский счет для списания
     * @param amount   сумма перевода
     * @param bankName название банка-получателя
     */
    public void processBankTransfer(BankAccountDto account, BigDecimal amount, String bankName) {
        Map<String, String> details = new HashMap<>();
        details.put("category", "Beauty");
        details.put("createdDate", LocalDateTime.now().minusDays(8).toString());
        details.put("bankName", bankName);

        processPayment(account, amount, bankTransferStrategy, details);
    }

    /**
     * Обрабатывает платеж через электронный кошелек.
     *
     * @param account  банковский счет для списания
     * @param amount   сумма платежа
     * @param walletId идентификатор электронного кошелька
     */
    public void processWalletPayment(BankAccountDto account, BigDecimal amount, String walletId) {
        Map<String, String> details = new HashMap<>();
        details.put("category", "Health");
        details.put("createdDate", LocalDateTime.now().minusDays(6).toString());
        details.put("walletId", walletId);

        processPayment(account, amount, digitalWalletStrategy, details);
    }

    /**
     * Создает тестовый банковский счет с демонстрационными данными.
     *
     * @return объект {@link BankAccountDto} с тестовыми данными
     */
    public static BankAccountDto createTestAccount() {
        UserDto testUserDto = new UserDto();
        testUserDto.setId("user123");
        testUserDto.setFirstName("John");
        testUserDto.setMiddleName("K");
        testUserDto.setLastName("Doe");
        testUserDto.setBirthDate(LocalDateTime.now().minusYears(25).toLocalDate());
        testUserDto.setEmail("john.doe@example.com");
        testUserDto.setPhoneNumber("+1234567890");

        BankAccountDto account = new BankAccountDto();
        account.setId("acc123");
        account.setBalance(new BigDecimal("5000.00"));
        account.setOwner(testUserDto);

        TransactionDto transactionDto1 = TransactionDto.hiddenBuilder()
                .id("tx001")
                .amount(new BigDecimal("100.00"))
                .type(TransactionType.PAYMENT)
                .category("Electronics")
                .createdDate(LocalDateTime.now().minusDays(5))
                .build();

        TransactionDto transactionDto2 = TransactionDto.hiddenBuilder()
                .id("tx002")
                .amount(new BigDecimal("200.00"))
                .type(TransactionType.DEPOSIT)
                .category("Groceries")
                .createdDate(LocalDateTime.now().minusDays(2))
                .build();

        account.getTransactionDtos().addAll(List.of(transactionDto1, transactionDto2));

        return account;
    }
}