package gigabank.accountmanagement.service.proxy;

import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.service.BankAccountService;

import java.math.BigDecimal;
import java.util.Random;

public class SecurityLoggingProxy {
    private final BankAccountService bankAccountService;
    private final Random random;

    public SecurityLoggingProxy(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
        this.random = new Random();
    }

    public void processCardPayment(BankAccount account, BigDecimal amount, String cardNumber, String merchantName) {
        System.out.println("Проверка доступа для выполнения операции...");
        boolean accessGranted = random.nextBoolean();

        if (accessGranted) {
            System.out.println("Доступ разрешён. Выполняем операцию...");
            bankAccountService.processCardPayment(account, amount, cardNumber, merchantName);
        } else {
            System.out.println("Доступ запрещён. Операция отменена.");
        }
    }

    public void processBankTransfer(BankAccount account, BigDecimal amount, String bankName) {
        System.out.println("Проверка доступа для выполнения операции...");
        boolean accessGranted = random.nextBoolean();

        if (accessGranted) {
            System.out.println("Доступ разрешён. Выполняем операцию...");
            bankAccountService.processBankTransfer(account, amount, bankName);
        } else {
            System.out.println("Доступ запрещён. Операция отменена.");
        }
    }

    public void processWalletPayment(BankAccount account, BigDecimal amount, String walletId) {
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

