package gigabank.accountmanagement;

import gigabank.accountmanagement.models.dto.BankAccount;
import gigabank.accountmanagement.models.dto.UserRequest;
import gigabank.accountmanagement.service.BankAccountService;
import gigabank.accountmanagement.design.factory.PaymentStrategyFactory;
import gigabank.accountmanagement.design.strategy.payment.bank.manager.PaymentStrategy;

import java.util.List;

/**
 * Сервис для управления банковскими операциями, включая платежи и уведомления.
 */
public class BankAccountManager {
    private PaymentStrategyFactory paymentStrategyFactory;
    private BankAccountService bankAccountService;

    /**
     * Обработка списка запросов на платежи.
     * Для каждого запроса выполняется авторизация, списание средств с банковского счета и отправка уведомлений.
     *
     * @param userRequests список запросов на выполнение платежей
     */
    public void doWork(List<UserRequest> userRequests) {
        for (UserRequest request : userRequests) {
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