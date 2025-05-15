package gigabank.accountmanagement.service.factory;

import gigabank.accountmanagement.service.strategy.payment.bank.manager.BankPaymentStrategy;
import gigabank.accountmanagement.service.strategy.payment.bank.manager.CardPaymentStrategy;
import gigabank.accountmanagement.service.strategy.payment.bank.manager.PaymentStrategy;
import gigabank.accountmanagement.service.strategy.payment.bank.manager.WalletPaymentStrategy;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentStrategyFactory {

    public PaymentStrategy getPaymentStrategy(String paymentType) {
        return switch (paymentType) {
            case "CARD" -> new CardPaymentStrategy();
            case "BANK" -> new BankPaymentStrategy();
            case "WALLET" -> new WalletPaymentStrategy();
            default -> throw new IllegalArgumentException("Unsupported payment type: " + paymentType);
        };
    }
}
