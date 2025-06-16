package com.gigabank.service.transaction.strategy;

import com.gigabank.models.dto.request.account.AccountRequestDto;
import com.gigabank.models.dto.response.TransactionResponseDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>Стратегия обработки транзакций платежей через цифровые кошельки.</p>
 *
 * <p>Реализует {@link TransactionStrategy} для создания транзакции платежа через цифровой кошелек и добавления
 * ее в список транзакций на указанном банковском счете.</p>
 *
 * <p>Создает объект транзакции {@link TransactionResponseDto} на основе переданных данных и добавляет его в список транзакций счета.</p>
 */
@Service
public class DigitalWalletTransactionStrategy implements TransactionStrategy {

    /**
     * <p>Обрабатывает транзакцию платежа через цифровой кошелек.</p>
     *
     * <p>Создает {@link TransactionResponseDto} для платежа через цифровой кошелек, заполняя его соответствующими данными,
     * такими как: сумма, категория, дата создания и идентификатор кошелька. Затем добавляет эту транзакцию в список
     * транзакций счета.</p>
     *
     * @param accountDto банковский счет ({@link AccountRequestDto}), к которому добавляется транзакция
     * @param amount     сумма платежа ({@link BigDecimal})
     * @param details    дополнительные параметры перевода, такие как категория, дата, идентификатор кошелька
     */
    @Override
    public void process(AccountRequestDto accountDto, BigDecimal amount, Map<String, String> details) {
        TransactionResponseDto transactionResponseDtoWalletPayment = TransactionResponseDto.builder()
                .amount(amount)
                .category(details.get("category"))
                .createdDate(LocalDateTime.parse(details.get("createdDate")))
                .digitalWalletId(details.get("walletId"))
                .build();

        accountDto.getListTransactionResponseDto().add(transactionResponseDtoWalletPayment);
    }
}