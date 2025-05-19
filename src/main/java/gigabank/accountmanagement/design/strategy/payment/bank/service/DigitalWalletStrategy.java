package gigabank.accountmanagement.design.strategy.payment.bank.service;

import gigabank.accountmanagement.models.dto.BankAccount;
import gigabank.accountmanagement.service.PaymentGatewayService;
import gigabank.accountmanagement.design.adapter.NotificationAdapter;
import gigabank.accountmanagement.design.strategy.transaction.DigitalWalletTransactionStrategy;
import gigabank.accountmanagement.design.strategy.transaction.TransactionStrategy;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@NoArgsConstructor
public class DigitalWalletStrategy implements PaymentStrategy {

    private PaymentGatewayService paymentGatewayService;
    private NotificationAdapter notificationAdapter;

    @Override
    public void process(BankAccount account, BigDecimal amount, Map<String, String> details) {
        account.setBalance(account.getBalance().subtract(amount));

        processCreateTransaction(account, amount, new DigitalWalletTransactionStrategy(), details);

        System.out.println("Processed wallet payment for account " + account.getId());
        paymentGatewayService.authorize("Платеж по карте", amount);
        notificationAdapter.sendNotificationToUser(
                account.getOwner(),
                "Произошел платеж по карте",
                "Информация о платеже",
                "Произошел платеж по карте");
    }

    private void processCreateTransaction(BankAccount account, BigDecimal amount,
                                          TransactionStrategy strategy,
                                          Map<String, String> details) {
        strategy.process(account, amount, details);
    }
}