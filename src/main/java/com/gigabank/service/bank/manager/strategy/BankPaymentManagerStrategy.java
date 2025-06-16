package com.gigabank.service.bank.manager.strategy;

import com.gigabank.models.dto.request.account.AccountRequestDto;
import com.gigabank.models.dto.request.user.AnotherRequestDto;
import com.gigabank.service.PaymentGatewayService;
import com.gigabank.service.notification.NotificationAdapter;
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
     * @param accountDto банковский счет ({@link AccountRequestDto})
     * @param request    данные платежного запроса ({@link AnotherRequestDto})
     */
    @Override
    public void process(AccountRequestDto accountDto, AnotherRequestDto request) {
        paymentGatewayService.authorize("tx", request.getAmount());
        System.out.println("Bank payment for account " + accountDto.getId());
        notificationAdapter.sendAllNotificationToUser(
                accountDto.getUserResponseDto(),
                "Bank payment of",
                "Payment",
                "Bank payment of");
    }
}