package gigabank.accountmanagement.service.design.strategy.transaction;

import gigabank.accountmanagement.models.dto.BankAccountDto;
import gigabank.accountmanagement.models.dto.TransactionDto;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
public class CardPaymentTransactionStrategy implements TransactionStrategy {

    @Override
    public void process(BankAccountDto account, BigDecimal amount, Map<String, String> details) {
        TransactionDto transactionDtoCardPayment = TransactionDto.hiddenBuilder()
                .amount(amount)
                .category(details.get("category"))
                .createdDate(LocalDateTime.parse(details.get("createdDate")))
                .cardNumber(details.get("cardNumber"))
                .merchantName(details.get("merchantName"))
                .build();

        account.getTransactionDtos().add(transactionDtoCardPayment);
    }
}

