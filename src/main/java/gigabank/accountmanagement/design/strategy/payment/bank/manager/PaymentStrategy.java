package gigabank.accountmanagement.design.strategy.payment.bank.manager;

import gigabank.accountmanagement.models.dto.UserRequest;
import gigabank.accountmanagement.models.dto.BankAccount;

public interface PaymentStrategy {
    void process(BankAccount account, UserRequest request);
}
