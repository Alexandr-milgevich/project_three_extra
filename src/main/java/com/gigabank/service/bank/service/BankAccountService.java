package com.gigabank.service.bank.service;

import com.gigabank.constants.PaymentType;
import com.gigabank.constants.TransactionType;
import com.gigabank.models.dto.request.account.AccountRequestDto;
import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.models.dto.response.UserResponseDto;
import com.gigabank.service.bank.service.factory.PaymentServiceStrategyFactory;
import com.gigabank.service.bank.service.strategy.PaymentServiceStrategy;
import jakarta.transaction.Transactional;
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
     * Обрабатывает платеж с использованием указанной стратегии.
     *
     * @param accountDto банковский счет для списания
     * @param amount     сумма платежа
     * @param strategy   стратегия обработки платежа ({@link PaymentServiceStrategy})
     * @param details    дополнительные детали платежа
     */
    public void processPayment(AccountRequestDto accountDto, BigDecimal amount,
                               PaymentServiceStrategy strategy,
                               Map<String, String> details) {
        strategy.process(accountDto, amount, details);
    }

    /**
     * Снимает указанную сумму с банковского счета.
     *
     * @param accountDto банковский счет для списания
     * @param amount     сумма для снятия
     */
    @Transactional
    public void withdraw(AccountRequestDto accountDto, BigDecimal amount) {
        accountDto.setBalance(accountDto.getBalance().subtract(amount));
    }

    /**
     * Обрабатывает платеж банковской картой.
     *
     * @param accountDto   банковский счет для списания
     * @param amount       сумма платежа
     * @param cardNumber   последние 4 цифры номера карты
     * @param merchantName название мерчанта
     */
    public void processCardPayment(AccountRequestDto accountDto, BigDecimal amount,
                                   String cardNumber, String merchantName) {
        Map<String, String> details = new HashMap<>();
        details.put("category", "Education");
        details.put("createdDate", LocalDateTime.now().minusDays(10).toString());
        details.put("cardNumber", cardNumber);
        details.put("merchantName", merchantName);

        processPayment(accountDto,
                amount,
                paymentStrategyFactory.getPaymentStrategy(PaymentType.CARD),
                details);
    }

    /**
     * Обрабатывает банковский перевод.
     *
     * @param accountDto банковский счет для списания
     * @param amount     сумма перевода
     * @param bankName   название банка-получателя
     */
    public void processBankTransfer(AccountRequestDto accountDto, BigDecimal amount, String bankName) {
        Map<String, String> details = new HashMap<>();
        details.put("category", "Beauty");
        details.put("createdDate", LocalDateTime.now().minusDays(8).toString());
        details.put("bankName", bankName);

        processPayment(accountDto,
                amount,
                paymentStrategyFactory.getPaymentStrategy(PaymentType.BANK),
                details);
    }

    /**
     * Обрабатывает платеж через электронный кошелек.
     *
     * @param accountDto банковский счет для списания
     * @param amount     сумма платежа
     * @param walletId   идентификатор электронного кошелька
     */
    public void processWalletPayment(AccountRequestDto accountDto, BigDecimal amount, String walletId) {
        Map<String, String> details = new HashMap<>();
        details.put("category", "Health");
        details.put("createdDate", LocalDateTime.now().minusDays(6).toString());
        details.put("walletId", walletId);

        processPayment(accountDto,
                amount,
                paymentStrategyFactory.getPaymentStrategy(PaymentType.WALLET),
                details);
    }

    /**
     * Создает тестовый банковский счет с демонстрационными данными.
     */
    public static void createTestAccount() {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(0L);
        userResponseDto.setUsername("John");
        userResponseDto.setBirthDate(LocalDateTime.now().minusYears(25).toLocalDate());
        userResponseDto.setEmail("john.doe@example.com");
        userResponseDto.setPhoneNumber("+1234567890");

        AccountRequestDto accountDto = new AccountRequestDto();
        accountDto.setId(1L);
        accountDto.setBalance(new BigDecimal("5000.00"));
        accountDto.setUserResponseDto(userResponseDto);

        TransactionResponseDto transactionResponseDto1 = TransactionResponseDto.builder()
                .id(0L)
                .amount(new BigDecimal("100.00"))
                .type(TransactionType.PAYMENT)
                .category("Electronics")
                .createdDate(LocalDateTime.now().minusDays(5))
                .build();

        TransactionResponseDto transactionResponseDto2 = TransactionResponseDto.builder()
                .id(1L)
                .amount(new BigDecimal("200.00"))
                .type(TransactionType.DEPOSIT)
                .category("Groceries")
                .createdDate(LocalDateTime.now().minusDays(2))
                .build();

        accountDto.getListTransactionResponseDto().addAll(List.of(transactionResponseDto1, transactionResponseDto2));
    }
}