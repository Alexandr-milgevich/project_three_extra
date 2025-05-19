package gigabank.accountmanagement.service;

import gigabank.accountmanagement.models.dto.BankAccountDto;
import gigabank.accountmanagement.models.dto.UserRequestDto;
import gigabank.accountmanagement.service.design.strategy.payment.bank.manager.PaymentStrategy;
import gigabank.accountmanagement.service.design.strategy.payment.bank.manager.factory.PaymentStrategyFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Сервис для управления банковскими операциями, включая платежи и уведомления.
 */
@RequiredArgsConstructor
public class BankAccountManager {
    private final PaymentStrategyFactory paymentStrategyFactory;
    private final BankAccountService bankAccountService;

    /**
     * Обработка списка запросов на платежи.
     * Для каждого запроса выполняется авторизация, списание средств с банковского счета и отправка уведомлений.
     *
     * @param userRequestDtoList список запросов на выполнение платежей
     */
    public void doWork(List<UserRequestDto> userRequestDtoList) {
        for (UserRequestDto request : userRequestDtoList) {
            BankAccountDto account = bankAccountService.findAccountById(request.getAccountId());
            if (account == null) {
                System.out.println("No account found for ID: " + request.getAccountId());
                continue;
            }
            PaymentStrategy paymentStrategy = paymentStrategyFactory.getPaymentStrategy(request.getPaymentType());
            paymentStrategy.process(account, request);
        }
    }
}