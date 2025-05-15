package gigabank.accountmanagement.service;

import gigabank.accountmanagement.dto.UserRequest;
import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.service.strategy.payment.bank.manager.PaymentStrategy;
import gigabank.accountmanagement.service.factory.PaymentStrategyFactory;

import java.util.List;

/**
 * Сервис для управления банковскими операциями, включая платежи и уведомления.
 */
public class BankManager {
    private PaymentStrategyFactory paymentStrategyFactory;
    private BankAccountService bankAccountService;

    /**
     * Обработка списка запросов на платежи.
     * Для каждого запроса выполняется авторизация, списание средств с банковского счета и отправка уведомлений.
     *
     * @param rqs список запросов на выполнение платежей
     */
    public void doWork(List<UserRequest> rqs) {
        for (UserRequest request : rqs) {
            BankAccount account = bankAccountService.findAccountById(request.getAccountId());
            if (account == null) {
                System.out.println("No account found for ID: " + request.getAccountId());
                continue;
            }
            PaymentStrategy paymentStrategy = paymentStrategyFactory.getPaymentStrategy(request.getPaymentType());
            paymentStrategy.process(account, request);
        }
    }
}