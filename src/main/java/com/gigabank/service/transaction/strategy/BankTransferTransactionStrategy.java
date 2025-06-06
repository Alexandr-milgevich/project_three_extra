package com.gigabank.service.transaction.strategy;

import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.models.dto.request.account.AccountRequestDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;


/**
 * <p>Стратегия обработки транзакций банковских переводов.</p>
 *
 * <p>Реализует {@link TransactionStrategy} для создания транзакции банковского перевода и добавления ее в список
 * транзакций на указанном банковском счете.</p>
 *
 * <p>Создает объект транзакции {@link TransactionResponseDto} на основе переданных данных и добавляет его в список транзакций счета.</p>
 */
@Service
public class BankTransferTransactionStrategy implements TransactionStrategy {

    /**
     * <p>Обрабатывает транзакцию банковского перевода.</p>
     *
     * <p>Создает {@link TransactionResponseDto} для перевода средств, заполняя его соответствующими данными, такими как:
     * сумма, категория, дата создания, название банка. Затем добавляет эту транзакцию в список транзакций счета.</p>
     *
     * @param accountDto банковский счет ({@link AccountRequestDto}), к которому добавляется транзакция
     * @param amount     сумма перевода ({@link BigDecimal})
     * @param details    дополнительные параметры перевода, такие как категория, дата и банк
     */
    @Override
    public void process(AccountRequestDto accountDto, BigDecimal amount, Map<String, String> details) {
        TransactionResponseDto transactionResponseDtoBankTransfer = TransactionResponseDto.builder()
                .amount(amount)
                .category(details.get("category"))
                .createdDate(LocalDateTime.parse(details.get("createdDate")))
                .bankName(details.get("bankName"))
                .build();

        accountDto.getListTransactionResponseDto().add(transactionResponseDtoBankTransfer);
    }
}
