package gigabank.accountmanagement.design.factory;

import gigabank.accountmanagement.design.strategy.payment.bank.manager.BankPaymentStrategy;
import gigabank.accountmanagement.design.strategy.payment.bank.manager.CardPaymentStrategy;
import gigabank.accountmanagement.design.strategy.payment.bank.manager.PaymentStrategy;
import gigabank.accountmanagement.design.strategy.payment.bank.manager.WalletPaymentStrategy;
import lombok.NoArgsConstructor;

/**
 * Фабрика для создания стратегий оплаты.
 * Реализует паттерн "Фабрика" для инкапсуляции логики создания конкретных реализаций
 * {@link PaymentStrategy} в зависимости от типа платежа.
 * Поддерживаемые типы платежей:
 * {@link CardPaymentStrategy} (оплата картой)
 * {@link BankPaymentStrategy} (банковский перевод)
 * {@link WalletPaymentStrategy} (оплата через электронный кошелек)
 */
@NoArgsConstructor
public class PaymentStrategyFactory {

    /**
     * Создает и возвращает стратегию оплаты для указанного типа платежа.
     *
     * @param paymentType тип платежа (CARD, BANK, WALLET)
     * @return соответствующая реализация {@link PaymentStrategy}
     */
    public PaymentStrategy getPaymentStrategy(String paymentType) {
        return switch (paymentType) {
            case "CARD" -> new CardPaymentStrategy();
            case "BANK" -> new BankPaymentStrategy();
            case "WALLET" -> new WalletPaymentStrategy();
            default -> throw new IllegalArgumentException("Unsupported payment type: " + paymentType);
        };
    }
}