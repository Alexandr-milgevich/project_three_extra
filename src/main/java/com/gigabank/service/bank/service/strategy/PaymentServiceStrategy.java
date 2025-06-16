package com.gigabank.service.bank.service.strategy;

import com.gigabank.models.dto.request.account.AccountRequestDto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>Интерфейс для реализации стратегий обработки различных типов платежей.</p>
 *
 * <p>Каждая стратегия, реализующая этот интерфейс, должна предоставлять свою реализацию метода {@link #process(AccountRequestDto, BigDecimal, Map)}
 * для выполнения операции по переводу средств или другим операциям с платежами.</p>
 */
public interface PaymentServiceStrategy {
    /**
     * <p>Обрабатывает платежную операцию.</p>
     *
     * <p>Метод выполняет необходимые шаги для обработки конкретного типа платежа, например, списание средств,
     * создание транзакции и отправка уведомлений.</p>
     *
     * @param accountDto банковский счет ({@link AccountRequestDto}), с которого будет списана сумма
     * @param amount     сумма перевода ({@link BigDecimal})
     * @param details    дополнительные параметры перевода
     */
    void process(AccountRequestDto accountDto, BigDecimal amount, Map<String, String> details);
}