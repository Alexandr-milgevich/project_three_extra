package gigabank.accountmanagement.service.bank.manager.strategy;

import gigabank.accountmanagement.models.dto.Account;
import gigabank.accountmanagement.models.dto.UserRequestDto;
import gigabank.accountmanagement.service.PaymentGatewayService;
import gigabank.accountmanagement.service.bank.service.BankAccountService;
import gigabank.accountmanagement.service.notification.NotificationAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DigitalWalletPaymentManagerStrategy implements PaymentManagerStrategy {
    private final PaymentGatewayService paymentGatewayService;
    private final BankAccountService bankAccountService;
    private final NotificationAdapter notificationAdapter;

    /**
     * Обрабатывает банковский платеж.
     * <p>
     * Выполняет полный цикл обработки платежа:
     * 1. Авторизацию через платежный шлюз
     * 2. Выполнение банковского платежа
     * 3. Отправку уведомления пользователю
     * </p>
     *
     * @param account банковский счет ({@link Account})
     * @param request данные платежного запроса ({@link UserRequestDto})
     */
    @Override
    public void process(Account account, UserRequestDto request) {
        paymentGatewayService.authorize("tx", request.getAmount());
        bankAccountService.withdraw(account, request.getAmount());
        System.out.println("Wallet payment for account " + account.getId());
        notificationAdapter.sendAllNotificationToUser(
                account.getOwner(),
                "Wallet payment of",
                "Payment",
                "Wallet payment of");
    }
}