package com.gigabank.service.bank.manager;

import com.gigabank.mappers.AccountMapper;
import com.gigabank.models.dto.request.user.UserAnotherRequestDto;
import com.gigabank.models.dto.response.AccountResponseDto;
import com.gigabank.service.account.AccountService;
import com.gigabank.service.bank.manager.factory.PaymentManagerStrategyFactory;
import com.gigabank.service.bank.manager.strategy.PaymentManagerStrategy;
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
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    /**
     * Обрабатывает список платежных запросов.
     * <p>
     * Для каждого запроса выполняет:
     * <li>Поиск счета по идентификатору</li>
     * <li>Выбор стратегии обработки в зависимости от типа платежа</li>
     * <li>Выполнение платежа через выбранную стратегию</li>
     * </p>
     *
     * @param userAnotherRequestDtoList список платежных запросов ({@link UserAnotherRequestDto})
     */
    public void doWork(List<UserAnotherRequestDto> userAnotherRequestDtoList) {
        for (UserAnotherRequestDto request : userAnotherRequestDtoList) {
            AccountResponseDto accountDto = accountService.getAccountByIdFromController(request.getAccountId());
            if (accountDto == null) {
                System.out.println("No account found for ID: " + request.getAccountId());
                continue;
            }
            PaymentManagerStrategy paymentManagerStrategy = paymentManagerStrategyFactory
                    .getPaymentStrategy(request.getPaymentType());
            paymentManagerStrategy.process(accountMapper.toRequestDto(accountDto), request);
        }
    }
}