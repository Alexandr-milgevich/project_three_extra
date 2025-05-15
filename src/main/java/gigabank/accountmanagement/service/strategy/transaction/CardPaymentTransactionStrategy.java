package gigabank.accountmanagement.service.strategy.transaction;

import gigabank.accountmanagement.entity.BankAccount;
import gigabank.accountmanagement.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public class CardPaymentTransactionStrategy implements TransactionStrategy {
    public CardPaymentTransactionStrategy() {
    }

    @Override
    public void process(BankAccount account, BigDecimal amount, Map<String, String> details) {
        Transaction transactionCardPayment = Transaction.hiddenBuilder()
                .amount(amount)
                .category(details.get("category"))
                .createdDate(LocalDateTime.parse(details.get("createdDate")))
                .cardNumber(details.get("cardNumber"))
                .merchantName(details.get("merchantName"))
                .build();

        account.getTransactions().add(transactionCardPayment);
    }
}

