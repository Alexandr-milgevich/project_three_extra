package gigabank.accountmanagement.service.bank.service.strategy;

import gigabank.accountmanagement.models.dto.Account;
import gigabank.accountmanagement.service.PaymentGatewayService;
import gigabank.accountmanagement.service.notification.NotificationAdapter;
import gigabank.accountmanagement.service.transaction.strategy.CardPaymentTransactionStrategy;
import gigabank.accountmanagement.service.transaction.strategy.TransactionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>Стратегия обработки платежей по картам.</p>
 *
 * <p>Реализует {@link PaymentServiceStrategy} для выполнения операций по платежам с использованием карты.
 * Включает полный цикл обработки:</p>
 *
 * <ul>
 *   <li>Списание средств с баланса счета</li>
 *   <li>Создание транзакции через {@link CardPaymentTransactionStrategy}</li>
 *   <li>Авторизация операции через {@link PaymentGatewayService}</li>
 *   <li>Отправка уведомления через {@link NotificationAdapter}</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class CardPaymentServiceStrategy implements PaymentServiceStrategy {

    private final PaymentGatewayService paymentGatewayService;
    private final NotificationAdapter notificationAdapter;

    /**
     * <p>Выполняет обработку платежа по карте.</p>
     *
     * <p>Основные шаги:</p>
     * <ul>
     *   <li>Списание суммы с баланса счета {@link Account}</li>
     *   <li>Создание транзакции платежа</li>
     *   <li>Авторизация операции</li>
     *   <li>Отправка уведомления клиенту</li>
     * </ul>
     *
     * @param account банковский счет ({@link Account})
     * @param amount  сумма платежа ({@link BigDecimal})
     * @param details дополнительные параметры платежа
     */
    @Override
    public void process(Account account, BigDecimal amount, Map<String, String> details) {
        account.setBalance(account.getBalance().subtract(amount));

        processCreateTransaction(account, amount, new CardPaymentTransactionStrategy(), details);

        System.out.println("Processed card payment for account " + account.getId());
        paymentGatewayService.authorize("Платеж по карте", amount);
        notificationAdapter.sendAllNotificationToUser(
                account.getOwner(),
                "Произошел платеж по карте",
                "Информация о платеже",
                "Произошел платеж по карте");
    }

    /**
     * <p>Создает транзакцию платежа по карте.</p>
     *
     * <p>Использует указанную стратегию {@link TransactionStrategy}
     * для обработки транзакции.</p>
     *
     * @param account  банковский счет ({@link Account})
     * @param amount   сумма платежа ({@link BigDecimal})
     * @param strategy стратегия обработки транзакции
     * @param details  дополнительные параметры платежа
     */
    private void processCreateTransaction(Account account, BigDecimal amount,
                                          TransactionStrategy strategy,
                                          Map<String, String> details) {
        strategy.process(account, amount, details);
    }
}