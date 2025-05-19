package gigabank.accountmanagement.design.strategy.transaction;

import gigabank.accountmanagement.models.dto.BankAccount;

import java.math.BigDecimal;
import java.util.Map;

public interface TransactionStrategy {
    void process(BankAccount account, BigDecimal amount, Map<String, String> details);
}