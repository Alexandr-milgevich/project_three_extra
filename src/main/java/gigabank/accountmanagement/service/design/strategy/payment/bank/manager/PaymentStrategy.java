package gigabank.accountmanagement.service.design.strategy.payment.bank.manager;

import gigabank.accountmanagement.models.dto.UserRequestDto;
import gigabank.accountmanagement.models.dto.BankAccountDto;

public interface PaymentStrategy {
    void process(BankAccountDto account, UserRequestDto request);
}
