package com.gigabank.service.bank.manager.strategy;

import com.gigabank.models.dto.request.account.AccountRequestDto;
import com.gigabank.models.dto.request.user.AnotherRequestDto;
import com.gigabank.service.PaymentGatewayService;
import com.gigabank.service.bank.service.BankAccountService;
import com.gigabank.service.notification.NotificationAdapter;
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
     * @param accountDto банковский счет ({@link AccountRequestDto})
     * @param request    данные платежного запроса ({@link AnotherRequestDto})
     */
    @Override
    public void process(AccountRequestDto accountDto, AnotherRequestDto request) {
        paymentGatewayService.authorize("tx", request.getAmount());
        bankAccountService.withdraw(accountDto, request.getAmount());
        System.out.println("Wallet payment for account " + accountDto.getId());
        notificationAdapter.sendAllNotificationToUser(
                accountDto.getUserResponseDto(),
                "Wallet payment of",
                "Payment",
                "Wallet payment of");
    }
}