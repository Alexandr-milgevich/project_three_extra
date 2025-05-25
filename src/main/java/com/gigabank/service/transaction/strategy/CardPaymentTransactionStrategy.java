package com.gigabank.service.transaction.strategy;

import com.gigabank.models.dto.AccountDto;
import com.gigabank.models.dto.TransactionDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;


/**
 * <p>Стратегия обработки транзакций платежей по карте.</p>
 *
 * <p>Реализует {@link TransactionStrategy} для создания транзакции платежа по карте и добавления ее в список
 * транзакций на указанном банковском счете.</p>
 *
 * <p>Создает объект транзакции {@link TransactionDto} на основе переданных данных и добавляет его в список транзакций счета.</p>
 */
@Service
public class CardPaymentTransactionStrategy implements TransactionStrategy {

    /**
     * <p>Обрабатывает транзакцию платежа по карте.</p>
     *
     * <p>Создает {@link TransactionDto} для платежа по карте, заполняя его соответствующими данными, такими как:
     * сумма, категория, дата создания, номер карты и имя торговца. Затем добавляет эту транзакцию в список транзакций счета.</p>
     *
     * @param accountDto банковский счет ({@link AccountDto}), к которому добавляется транзакция
     * @param amount сумма платежа ({@link BigDecimal})
     * @param details дополнительные параметры перевода, такие как категория, дата, номер карты и имя торговца
     */
    @Override
    public void process(AccountDto accountDto, BigDecimal amount, Map<String, String> details) {
        TransactionDto transactionDtoCardPayment = TransactionDto.builder()
                .amount(amount)
                .category(details.get("category"))
                .createdDate(LocalDateTime.parse(details.get("createdDate")))
                .cardNumber(details.get("cardNumber"))
                .merchantName(details.get("merchantName"))
                .build();

        accountDto.getListTransactionDto().add(transactionDtoCardPayment);
    }
}

