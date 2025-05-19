package gigabank.accountmanagement.design.strategy.payment.bank.service;

import gigabank.accountmanagement.models.dto.BankAccount;
import gigabank.accountmanagement.service.PaymentGatewayService;
import gigabank.accountmanagement.design.adapter.NotificationAdapter;
import gigabank.accountmanagement.design.strategy.transaction.BankTransferTransactionStrategy;
import gigabank.accountmanagement.design.strategy.transaction.TransactionStrategy;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@NoArgsConstructor
public class BankTransferStrategy implements PaymentStrategy {

    private PaymentGatewayService paymentGatewayService;
    private NotificationAdapter notificationAdapter;

    @Override
    public void process(BankAccount account, BigDecimal amount, Map<String, String> details) {
        account.setBalance(account.getBalance().subtract(amount));
        processCreateTransaction(account,amount, new BankTransferTransactionStrategy(),details);

        System.out.println("Processed bank transfer for account " + account.getId());
        paymentGatewayService.authorize("Банковский перевод", amount);
        notificationAdapter.sendNotificationToUser(
                account.getOwner(),
                "Произошел банковский перевод",
                "Информация о платеже",
                "Произошел банковский перевод");
    }

    private void processCreateTransaction(BankAccount account, BigDecimal amount,
                                          TransactionStrategy strategy,
                                          Map<String, String> details) {
        strategy.process(account, amount, details);
    }
}