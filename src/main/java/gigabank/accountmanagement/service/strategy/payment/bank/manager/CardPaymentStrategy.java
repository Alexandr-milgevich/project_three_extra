package gigabank.accountmanagement.service.strategy.payment.bank.manager;

import gigabank.accountmanagement.dto.UserRequest;
import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.service.BankAccountService;
import gigabank.accountmanagement.service.PaymentGatewayService;
import gigabank.accountmanagement.service.notification.adapter.NotificationAdapter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CardPaymentStrategy implements PaymentStrategy {
    private PaymentGatewayService paymentGatewayService;
    private BankAccountService bankAccountService;
    private NotificationAdapter notificationAdapter;

    @Override
    public void process(BankAccount account, UserRequest request) {
        paymentGatewayService.authorize("tx", request.getAmount());
        bankAccountService.withdraw(account, request.getAmount());
        System.out.println("Card payment for account " + account.getId());
        notificationAdapter.sendNotificationToUser(
                account.getOwner(),
                "Paid",
                "Payment",
                "Card payment of");
    }
}
