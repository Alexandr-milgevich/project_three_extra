package gigabank.accountmanagement.service.design.strategy.payment.bank.service;

import gigabank.accountmanagement.models.dto.BankAccountDto;

import java.math.BigDecimal;
import java.util.Map;

public interface PaymentStrategy {
    void process(BankAccountDto account, BigDecimal amount, Map<String, String> details);
}