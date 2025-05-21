package gigabank.accountmanagement.service.bank.service.strategy;

import gigabank.accountmanagement.models.dto.Account;
import gigabank.accountmanagement.service.PaymentGatewayService;
import gigabank.accountmanagement.service.notification.NotificationAdapter;
import gigabank.accountmanagement.service.transaction.strategy.BankTransferTransactionStrategy;
import gigabank.accountmanagement.service.transaction.strategy.TransactionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>Стратегия обработки банковских переводов.</p>
 *
 * <p>Реализует {@link PaymentServiceStrategy} для выполнения операций по переводу средств между счетами.
 * Включает полный цикл обработки:</p>
 *
 * <ul>
 *   <li>Списание средств с баланса счета</li>
 *   <li>Создание транзакции через {@link BankTransferTransactionStrategy}</li>
 *   <li>Авторизация операции через {@link PaymentGatewayService}</li>
 *   <li>Отправка уведомления через {@link NotificationAdapter}</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class BankTransferServiceStrategy implements PaymentServiceStrategy {

    private final PaymentGatewayService paymentGatewayService;
    private final NotificationAdapter notificationAdapter;

    /**
     * <p>Выполняет обработку банковского перевода.</p>
     *
     * <p>Основные шаги:</p>
     * <ul>
     *   <li>Списание суммы с баланса счета {@link Account}</li>
     *   <li>Создание транзакции перевода</li>
     *   <li>Авторизация операции</li>
     *   <li>Отправка уведомления клиенту</li>
     * </ul>
     *
     * @param account банковский счет ({@link Account})
     * @param amount  сумма перевода ({@link BigDecimal})
     * @param details дополнительные параметры перевода
     */
    @Override
    public void process(Account account, BigDecimal amount, Map<String, String> details) {
        account.setBalance(account.getBalance().subtract(amount));
        processCreateTransaction(account, amount, new BankTransferTransactionStrategy(), details);

        System.out.println("Processed bank transfer for account " + account.getId());
        paymentGatewayService.authorize("Банковский перевод", amount);
        notificationAdapter.sendAllNotificationToUser(
                account.getOwner(),
                "Произошел банковский перевод",
                "Информация о платеже",
                "Произошел банковский перевод");
    }

    /**
     * <p>Создает транзакцию перевода средств.</p>
     *
     * <p>Использует указанную стратегию {@link TransactionStrategy}
     * для обработки транзакции.</p>
     *
     * @param account  банковский счет ({@link Account})
     * @param amount   сумма перевода ({@link BigDecimal})
     * @param strategy стратегия обработки транзакции
     * @param details  дополнительные параметры перевода
     */
    private void processCreateTransaction(Account account, BigDecimal amount,
                                          TransactionStrategy strategy,
                                          Map<String, String> details) {
        strategy.process(account, amount, details);
    }
}