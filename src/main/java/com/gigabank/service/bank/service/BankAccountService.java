package com.gigabank.service.bank.service;

import com.gigabank.constants.PaymentType;
import com.gigabank.constants.TransactionCategories;
import com.gigabank.constants.TransactionType;
import com.gigabank.models.dto.request.account.AccountRequestDto;
import com.gigabank.models.entity.BankAccount;
import com.gigabank.models.entity.Transaction;
import com.gigabank.models.entity.User;
import com.gigabank.service.bank.service.factory.PaymentServiceStrategyFactory;
import com.gigabank.service.bank.service.strategy.PaymentServiceStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    public void processPayment(AccountRequestDto accountDto, BigDecimal amount,PaymentServiceStrategy strategy,
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
     * @param accountDto банковский счет для списания
     * @param amount     сумма платежа
     */
    public void processCardPayment(AccountRequestDto accountDto, BigDecimal amount,
                                   Map<String, String> details) {
//        details.put("cardNumber", cardNumber);
//        details.put("merchantName", merchantName);
        processPayment(accountDto, amount, paymentStrategyFactory.getPaymentStrategy(PaymentType.CARD), details);
    }

    /**
     * Обрабатывает банковский перевод.
     *
     * @param accountDto банковский счет для списания
     * @param amount     сумма перевода
     */
    public void processBankTransfer(AccountRequestDto accountDto, BigDecimal amount, Map<String, String> details) {
//        details.put("bankName", bankName);
        processPayment(accountDto, amount, paymentStrategyFactory.getPaymentStrategy(PaymentType.BANK), details);
    }

    /**
     * Обрабатывает платеж через электронный кошелек.
     *
     * @param accountDto банковский счет для списания
     * @param amount     сумма платежа
     */
    public void processWalletPayment(AccountRequestDto accountDto, BigDecimal amount, Map<String, String> details) {
//        details.put("walletId", walletId);
        processPayment(accountDto, amount, paymentStrategyFactory.getPaymentStrategy(PaymentType.WALLET), details);
    }

    /**
     * Создает тестовый банковский счет с демонстрационными данными.
     */
    public static void createTestAccount() {
        User user = User.builder()
                .username("John")
                .email("john@gmail.com")
                .phoneNumber("+1234567890")
                .build();

        BankAccount bankAccount = BankAccount.builder()
                .balance(new BigDecimal("5000.00"))
                .user(user)
                .build();

        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal("100.00"))
                .type(String.valueOf(TransactionType.PAYMENT))
                .category(String.valueOf(TransactionCategories.OTHER))
                .createdDate(LocalDateTime.now().minusDays(5))
                .build();

        Transaction transaction2 = Transaction.builder()
                .amount(new BigDecimal("200.00"))
                .type(String.valueOf(TransactionType.DEPOSIT))
                .category(String.valueOf(TransactionCategories.OTHER))
                .createdDate(LocalDateTime.now().minusDays(2))
                .build();

        bankAccount.getListTransactions().addAll(List.of(transaction, transaction2));
    }
}