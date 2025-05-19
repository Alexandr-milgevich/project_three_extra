package gigabank.accountmanagement.design.strategy.transaction;

import gigabank.accountmanagement.models.dto.BankAccount;
import gigabank.accountmanagement.models.dto.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class DigitalWalletTransactionStrategy implements TransactionStrategy {
    public DigitalWalletTransactionStrategy() {
    }

    @Override
    public void process(BankAccount account, BigDecimal amount, Map<String, String> details) {
        Transaction transactionWalletPayment = Transaction.hiddenBuilder()
                .amount(amount)
                .category(details.get("category"))
                .createdDate(LocalDateTime.parse(details.get("createdDate")))
                .digitalWalletId(details.get("walletId"))
                .build();

        account.getTransactions().add(transactionWalletPayment);
    }
}
