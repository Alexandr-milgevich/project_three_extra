package gigabank.accountmanagement.service.bank.manager.strategy;

import gigabank.accountmanagement.models.dto.Account;
import gigabank.accountmanagement.models.dto.UserRequestDto;
import gigabank.accountmanagement.service.PaymentGatewayService;
import gigabank.accountmanagement.service.notification.NotificationAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Стратегия обработки банковских платежей.
 * <p>
 * Реализует интерфейс {@link PaymentManagerStrategy} для выполнения банковских платежных операций.
 * Включает авторизацию платежа через платежный шлюз и отправку уведомления пользователю.
 * </p>
 *
 * <p><b>Основные функции:</b>
 * <ul>
 *   <li>Авторизация платежа через {@link PaymentGatewayService}</li>
 *   <li>Обработка банковского платежа</li>
 *   <li>Отправка уведомления через {@link NotificationAdapter}</li>
 * </ul>
 * </p>
 */
@Service
@RequiredArgsConstructor
public class BankPaymentManagerStrategy implements PaymentManagerStrategy {
    private final PaymentGatewayService paymentGatewayService;
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
        System.out.println("Bank payment for account " + account.getId());
        notificationAdapter.sendAllNotificationToUser(
                account.getOwner(),
                "Bank payment of",
                "Payment",
                "Bank payment of");
    }
}