package gigabank.accountmanagement.service.transaction.strategy;

import gigabank.accountmanagement.models.dto.Account;
import gigabank.accountmanagement.models.dto.TransactionDto;
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
 * <p>Создает объект транзакции {@link TransactionDto} на основе переданных данных и добавляет его в список транзакций счета.</p>
 */
@Service
public class DigitalWalletTransactionStrategy implements TransactionStrategy {

    /**
     * <p>Обрабатывает транзакцию платежа через цифровой кошелек.</p>
     *
     * <p>Создает {@link TransactionDto} для платежа через цифровой кошелек, заполняя его соответствующими данными,
     * такими как: сумма, категория, дата создания и идентификатор кошелька. Затем добавляет эту транзакцию в список
     * транзакций счета.</p>
     *
     * @param account банковский счет ({@link Account}), к которому добавляется транзакция
     * @param amount сумма платежа ({@link BigDecimal})
     * @param details дополнительные параметры перевода, такие как категория, дата, идентификатор кошелька
     */
    @Override
    public void process(Account account, BigDecimal amount, Map<String, String> details) {
        TransactionDto transactionDtoWalletPayment = TransactionDto.builder()
                .amount(amount)
                .category(details.get("category"))
                .createdDate(LocalDateTime.parse(details.get("createdDate")))
                .digitalWalletId(details.get("walletId"))
                .build();

        account.getTransactionDto().add(transactionDtoWalletPayment);
    }
}
