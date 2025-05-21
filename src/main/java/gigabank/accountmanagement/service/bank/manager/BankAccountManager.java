package gigabank.accountmanagement.service.bank.manager;

import gigabank.accountmanagement.models.dto.Account;
import gigabank.accountmanagement.models.dto.UserRequestDto;
import gigabank.accountmanagement.service.bank.manager.strategy.PaymentManagerStrategy;
import gigabank.accountmanagement.service.bank.manager.factory.PaymentManagerStrategyFactory;
import gigabank.accountmanagement.service.bank.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Основной сервис для управления банковскими операциями.
 * <p>
 * Координирует процесс выполнения платежей:
 * <li>Поиск банковских счетов</li>
 * <li>Выбор соответствующей стратегии обработки платежа</li>
 * <li>Выполнение цепочки операций по платежу</li>
 * </p>
 */
@Service
@RequiredArgsConstructor
public class BankAccountManager {
    private final PaymentManagerStrategyFactory paymentManagerStrategyFactory;
    private final BankAccountService bankAccountService;

    /**
     * Обрабатывает список платежных запросов.
     * <p>
     * Для каждого запроса выполняет:
     * <li>Поиск счета по идентификатору</li>
     * <li>Выбор стратегии обработки в зависимости от типа платежа</li>
     * <li>Выполнение платежа через выбранную стратегию</li>
     * </p>
     *
     * @param userRequestDtoList список платежных запросов ({@link UserRequestDto})
     */
    public void doWork(List<UserRequestDto> userRequestDtoList) {
        for (UserRequestDto request : userRequestDtoList) {
            Account account = bankAccountService.findAccountById(request.getAccountId());
            if (account == null) {
                System.out.println("No account found for ID: " + request.getAccountId());
                continue;
            }
            PaymentManagerStrategy paymentManagerStrategy = paymentManagerStrategyFactory.getPaymentStrategy(request.getPaymentType());
            paymentManagerStrategy.process(account, request);
        }
    }
}