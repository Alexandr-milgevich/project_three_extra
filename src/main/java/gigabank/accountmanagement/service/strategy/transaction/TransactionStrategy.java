package gigabank.accountmanagement.service.strategy.transaction;

import gigabank.accountmanagement.entity.BankAccount;

import java.math.BigDecimal;
import java.util.Map;

public interface TransactionStrategy {
    void process(BankAccount account, BigDecimal amount, Map<String, String> details);
}