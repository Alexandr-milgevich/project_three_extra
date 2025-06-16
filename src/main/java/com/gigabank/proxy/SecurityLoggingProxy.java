package com.gigabank.proxy;

import com.gigabank.models.dto.request.account.AccountRequestDto;
import com.gigabank.service.bank.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;

/**
 * Прокси-класс для безопасного выполнения операций с банковскими счетами.
 * В текущей реализации проверка доступа эмулируется случайным boolean-значением.
 */
@Service
@RequiredArgsConstructor
public class SecurityLoggingProxy {
    private final BankAccountService bankAccountService;

    private final Random random = new Random();

    /**
     * Обрабатывает платеж картой с проверкой доступа.
     *
     * @param accountDto   банковский счет для списания
     * @param amount       сумма платежа
     */
    public void processCardPayment(AccountRequestDto accountDto, BigDecimal amount, Map<String, String> details) {
        System.out.println("Проверка доступа для выполнения операции...");
        boolean accessGranted = random.nextBoolean();

        if (accessGranted) {
            System.out.println("Доступ разрешён. Выполняем операцию...");
            bankAccountService.processCardPayment(accountDto, amount, details);
        } else {
            System.out.println("Доступ запрещён. Операция отменена.");
        }
    }

    /**
     * Обрабатывает банковский перевод с проверкой доступа.
     *
     * @param accountDto банковский счет для списания
     * @param amount     сумма перевода
     */
    public void processBankTransfer(AccountRequestDto accountDto, BigDecimal amount, Map<String, String> details) {
        System.out.println("Проверка доступа для выполнения операции...");
        boolean accessGranted = random.nextBoolean();

        if (accessGranted) {
            System.out.println("Доступ разрешён. Выполняем операцию...");
            bankAccountService.processBankTransfer(accountDto, amount, details);
        } else {
            System.out.println("Доступ запрещён. Операция отменена.");
        }
    }

    /**
     * Обрабатывает платеж через электронный кошелек с проверкой доступа.
     *
     * @param accountDto банковский счет для списания
     * @param amount     сумма платежа
     */
    public void processWalletPayment(AccountRequestDto accountDto, BigDecimal amount, Map<String, String> details) {
        System.out.println("Проверка доступа для выполнения операции...");
        boolean accessGranted = random.nextBoolean();

        if (accessGranted) {
            System.out.println("Доступ разрешён. Выполняем операцию...");
            bankAccountService.processWalletPayment(accountDto, amount, details);
        } else {
            System.out.println("Доступ запрещён. Операция отменена.");
        }
    }
}