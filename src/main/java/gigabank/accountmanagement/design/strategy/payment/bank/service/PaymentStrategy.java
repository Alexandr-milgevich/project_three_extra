package gigabank.accountmanagement.design.strategy.payment.bank.service;

import gigabank.accountmanagement.models.dto.BankAccount;

import java.math.BigDecimal;
import java.util.Map;

public interface PaymentStrategy {
    void process(BankAccount account, BigDecimal amount, Map<String, String> details);
}