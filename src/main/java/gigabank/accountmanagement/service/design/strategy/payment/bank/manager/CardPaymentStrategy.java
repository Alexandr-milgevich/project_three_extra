package gigabank.accountmanagement.service.design.strategy.payment.bank.manager;

import gigabank.accountmanagement.models.dto.BankAccountDto;
import gigabank.accountmanagement.models.dto.UserRequestDto;
import gigabank.accountmanagement.service.BankAccountService;
import gigabank.accountmanagement.service.PaymentGatewayService;
import gigabank.accountmanagement.service.notification.adapter.NotificationAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardPaymentStrategy implements PaymentStrategy {
    private final PaymentGatewayService paymentGatewayService;
    private final BankAccountService bankAccountService;
    private final NotificationAdapter notificationAdapter;

    @Override
    public void process(BankAccountDto account, UserRequestDto request) {
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
