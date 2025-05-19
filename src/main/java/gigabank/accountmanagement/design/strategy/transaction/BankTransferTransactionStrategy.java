package gigabank.accountmanagement.design.strategy.transaction;

import gigabank.accountmanagement.models.dto.BankAccount;
import gigabank.accountmanagement.models.dto.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class BankTransferTransactionStrategy implements TransactionStrategy {

    public BankTransferTransactionStrategy() {
    }

    @Override
    public void process(BankAccount account, BigDecimal amount, Map<String, String> details) {
        Transaction transactionBankTransfer = Transaction.hiddenBuilder()
                .amount(amount)
                .category(details.get("category"))
                .createdDate(LocalDateTime.parse(details.get("createdDate")))
                .bankName(details.get("bankName"))
                .build();

        account.getTransactions().add(transactionBankTransfer);
    }
}
