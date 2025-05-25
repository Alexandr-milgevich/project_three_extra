package com.gigabank.service.bank.service.strategy;

import com.gigabank.models.dto.AccountDto;
import com.gigabank.service.PaymentGatewayService;
import com.gigabank.service.notification.NotificationAdapter;
import com.gigabank.service.transaction.strategy.CardPaymentTransactionStrategy;
import com.gigabank.service.transaction.strategy.TransactionStrategy;
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
     *   <li>Списание суммы с баланса счета {@link AccountDto}</li>
     *   <li>Создание транзакции платежа</li>
     *   <li>Авторизация операции</li>
     *   <li>Отправка уведомления клиенту</li>
     * </ul>
     *
     * @param accountDto банковский счет ({@link AccountDto})
     * @param amount  сумма платежа ({@link BigDecimal})
     * @param details дополнительные параметры платежа
     */
    @Override
    public void process(AccountDto accountDto, BigDecimal amount, Map<String, String> details) {
        accountDto.setBalance(accountDto.getBalance().subtract(amount));

        processCreateTransaction(accountDto, amount, new CardPaymentTransactionStrategy(), details);

        System.out.println("Processed card payment for account " + accountDto.getId());
        paymentGatewayService.authorize("Платеж по карте", amount);
        notificationAdapter.sendAllNotificationToUser(
                accountDto.getUserDto(),
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
     * @param accountDto  банковский счет ({@link AccountDto})
     * @param amount   сумма платежа ({@link BigDecimal})
     * @param strategy стратегия обработки транзакции
     * @param details  дополнительные параметры платежа
     */
    private void processCreateTransaction(AccountDto accountDto, BigDecimal amount,
                                          TransactionStrategy strategy,
                                          Map<String, String> details) {
        strategy.process(accountDto, amount, details);
    }
}