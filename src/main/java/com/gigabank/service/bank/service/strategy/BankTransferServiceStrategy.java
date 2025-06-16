package com.gigabank.service.bank.service.strategy;

import com.gigabank.models.dto.request.account.AccountRequestDto;
import com.gigabank.service.PaymentGatewayService;
import com.gigabank.service.notification.NotificationAdapter;
import com.gigabank.service.transaction.strategy.BankTransferTransactionStrategy;
import com.gigabank.service.transaction.strategy.TransactionStrategy;
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
     *   <li>Списание суммы с баланса счета {@link AccountRequestDto}</li>
     *   <li>Создание транзакции перевода</li>
     *   <li>Авторизация операции</li>
     *   <li>Отправка уведомления клиенту</li>
     * </ul>
     *
     * @param accountDto банковский счет ({@link AccountRequestDto})
     * @param amount     сумма перевода ({@link BigDecimal})
     * @param details    дополнительные параметры перевода
     */
    @Override
    public void process(AccountRequestDto accountDto, BigDecimal amount, Map<String, String> details) {
        accountDto.setBalance(accountDto.getBalance().subtract(amount));
        processCreateTransaction(accountDto, amount, new BankTransferTransactionStrategy(), details);

        System.out.println("Processed bank transfer for account " + accountDto.getId());
        paymentGatewayService.authorize("Банковский перевод", amount);
        notificationAdapter.sendAllNotificationToUser(
                accountDto.getUserResponseDto(),
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
     * @param accountDto банковский счет ({@link AccountRequestDto})
     * @param amount     сумма перевода ({@link BigDecimal})
     * @param strategy   стратегия обработки транзакции
     * @param details    дополнительные параметры перевода
     */
    private void processCreateTransaction(AccountRequestDto accountDto, BigDecimal amount,
                                          TransactionStrategy strategy,
                                          Map<String, String> details) {
        strategy.process(accountDto, amount, details);
    }
}