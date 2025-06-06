package com.gigabank.service.bank.manager.strategy;

import com.gigabank.models.dto.request.user.UserAnotherRequestDto;
import com.gigabank.models.dto.request.account.AccountRequestDto;
import com.gigabank.service.PaymentGatewayService;
import com.gigabank.service.bank.service.BankAccountService;
import com.gigabank.service.notification.NotificationAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Стратегия обработки платежей по банковской карте.
 * <p>
 * Реализация интерфейса {@link PaymentManagerStrategy} для обработки платежных операций,
 * совершаемых с использованием банковских карт. Включает полный цикл обработки платежа:
 * авторизацию, списание средств и уведомление клиента.
 * </p>
 * <p><b>Основные функции:</b>
 * <ul>
 *   <li>Авторизация платежа через {@link PaymentGatewayService}</li>
 *   <li>Списание средств со счета через {@link BankAccountService}</li>
 *   <li>Отправка уведомления клиенту через {@link NotificationAdapter}</li>
 * </ul>
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CardPaymentManagerStrategy implements PaymentManagerStrategy {
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
     * @param accountDto банковский счет ({@link AccountRequestDto}), с которого выполняется списание
     * @param request    данные платежного запроса ({@link UserAnotherRequestDto}), включая сумму платежа
     */
    @Override
    public void process(AccountRequestDto accountDto, UserAnotherRequestDto request) {
        paymentGatewayService.authorize("tx", request.getAmount());
        bankAccountService.withdraw(accountDto, request.getAmount());
        System.out.println("Card payment for account " + accountDto.getId());
        notificationAdapter.sendAllNotificationToUser(
                accountDto.getUserResponseDto(),
                "Paid",
                "Payment",
                "Card payment of");
    }
}