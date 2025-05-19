package gigabank.accountmanagement.design.strategy.payment.bank.manager;

import gigabank.accountmanagement.models.dto.dto.UserRequest;
import gigabank.accountmanagement.models.dto.BankAccount;
import gigabank.accountmanagement.service.BankAccountService;
import gigabank.accountmanagement.service.PaymentGatewayService;
import gigabank.accountmanagement.design.adapter.NotificationAdapter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BankPaymentStrategy implements PaymentStrategy {
    private PaymentGatewayService paymentGatewayService;
    private BankAccountService bankAccountService;
    private NotificationAdapter notificationAdapter;

    @Override
    public void process(BankAccount account, UserRequest request) {
        paymentGatewayService.authorize("tx", request.getAmount());
        System.out.println("Bank payment for account " + account.getId());
        notificationAdapter.sendNotificationToUser(
                account.getOwner(),
                "Bank payment of",
                "Payment",
                "Bank payment of");
    }
}