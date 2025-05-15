package gigabank.accountmanagement.service.strategy.payment.bank.manager;

import gigabank.accountmanagement.dto.UserRequest;
import gigabank.accountmanagement.entity.BankAccount;

public interface PaymentStrategy {
    void process(BankAccount account, UserRequest request);
}
