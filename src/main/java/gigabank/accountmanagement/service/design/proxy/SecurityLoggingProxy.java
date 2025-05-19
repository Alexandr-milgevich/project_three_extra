package gigabank.accountmanagement.service.design.proxy;

import gigabank.accountmanagement.models.dto.BankAccountDto;
import gigabank.accountmanagement.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Прокси-класс для безопасного выполнения операций с банковскими счетами.
 * В текущей реализации проверка доступа эмулируется случайным boolean-значением.
 */
@Component
@RequiredArgsConstructor
public class SecurityLoggingProxy {
    private final BankAccountService bankAccountService;

    private Random random = new Random();

    /**
     * Обрабатывает платеж картой с проверкой доступа.
     *
     * @param account      банковский счет для списания
     * @param amount       сумма платежа
     * @param cardNumber   номер карты (последние 4 цифры)
     * @param merchantName название мерчанта
     */
    public void processCardPayment(BankAccountDto account, BigDecimal amount, String cardNumber, String merchantName) {
        System.out.println("Проверка доступа для выполнения операции...");
        boolean accessGranted = random.nextBoolean();

        if (accessGranted) {
            System.out.println("Доступ разрешён. Выполняем операцию...");
            bankAccountService.processCardPayment(account, amount, cardNumber, merchantName);
        } else {
            System.out.println("Доступ запрещён. Операция отменена.");
        }
    }

    /**
     * Обрабатывает банковский перевод с проверкой доступа.
     *
     * @param account  банковский счет для списания
     * @param amount   сумма перевода
     * @param bankName название банка-получателя
     */
    public void processBankTransfer(BankAccountDto account, BigDecimal amount, String bankName) {
        System.out.println("Проверка доступа для выполнения операции...");
        boolean accessGranted = random.nextBoolean();

        if (accessGranted) {
            System.out.println("Доступ разрешён. Выполняем операцию...");
            bankAccountService.processBankTransfer(account, amount, bankName);
        } else {
            System.out.println("Доступ запрещён. Операция отменена.");
        }
    }

    /**
     * Обрабатывает платеж через электронный кошелек с проверкой доступа.
     *
     * @param account  банковский счет для списания
     * @param amount   сумма платежа
     * @param walletId идентификатор электронного кошелька
     */
    public void processWalletPayment(BankAccountDto account, BigDecimal amount, String walletId) {
        System.out.println("Проверка доступа для выполнения операции...");
        boolean accessGranted = random.nextBoolean();

        if (accessGranted) {
            System.out.println("Доступ разрешён. Выполняем операцию...");
            bankAccountService.processWalletPayment(account, amount, walletId);
        } else {
            System.out.println("Доступ запрещён. Операция отменена.");
        }
    }
}

