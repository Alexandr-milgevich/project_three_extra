package gigabank.accountmanagement.service.transaction.strategy;

import gigabank.accountmanagement.models.dto.Account;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>Интерфейс для реализации стратегий обработки транзакций.</p>
 *
 * <p>Каждая стратегия, реализующая этот интерфейс, должна предоставлять
 * свою реализацию метода {@link #process(Account, BigDecimal, Map)}
 * для обработки различных типов транзакций, например,
 * банковских переводов, платежей по карте или через цифровые кошельки.</p>
 */
public interface TransactionStrategy {
    /**
     * <p>Обрабатывает транзакцию.</p>
     *
     * <p>Метод выполняет необходимые действия для создания и записи транзакции в систему.
     * Каждая реализация этого метода
     * будет различаться в зависимости от типа транзакции (перевод средств, оплата по карте,
     * использование цифрового кошелька и т.д.).</p>
     *
     * @param account банковский счет ({@link Account}), с которого происходит транзакция
     * @param amount сумма транзакции ({@link BigDecimal})
     * @param details дополнительные параметры транзакции (например, категория, дата, номер карты и т.д.)
     */
    void process(Account account, BigDecimal amount, Map<String, String> details);
}