package gigabank.accountmanagement.service.design.strategy.transaction;

import gigabank.accountmanagement.models.dto.BankAccountDto;

import java.math.BigDecimal;
import java.util.Map;

public interface TransactionStrategy {
    void process(BankAccountDto account, BigDecimal amount, Map<String, String> details);
}