package gigabank.accountmanagement.service.design.strategy.payment.bank.service;

import gigabank.accountmanagement.models.dto.BankAccountDto;
import gigabank.accountmanagement.service.PaymentGatewayService;
import gigabank.accountmanagement.service.design.strategy.transaction.BankTransferTransactionStrategy;
import gigabank.accountmanagement.service.design.strategy.transaction.TransactionStrategy;
import gigabank.accountmanagement.service.notification.adapter.NotificationAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BankTransferStrategy implements PaymentStrategy {

    private final PaymentGatewayService paymentGatewayService;
    private final NotificationAdapter notificationAdapter;

    @Override
    public void process(BankAccountDto account, BigDecimal amount, Map<String, String> details) {
        account.setBalance(account.getBalance().subtract(amount));
        processCreateTransaction(account, amount, new BankTransferTransactionStrategy(), details);

        System.out.println("Processed bank transfer for account " + account.getId());
        paymentGatewayService.authorize("Банковский перевод", amount);
        notificationAdapter.sendNotificationToUser(
                account.getOwner(),
                "Произошел банковский перевод",
                "Информация о платеже",
                "Произошел банковский перевод");
    }

    private void processCreateTransaction(BankAccountDto account, BigDecimal amount,
                                          TransactionStrategy strategy,
                                          Map<String, String> details) {
        strategy.process(account, amount, details);
    }
}