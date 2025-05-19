package gigabank.accountmanagement.service.design.strategy.payment.bank.manager.factory;

import gigabank.accountmanagement.service.design.strategy.payment.bank.manager.BankPaymentStrategy;
import gigabank.accountmanagement.service.design.strategy.payment.bank.manager.CardPaymentStrategy;
import gigabank.accountmanagement.service.design.strategy.payment.bank.manager.PaymentStrategy;
import gigabank.accountmanagement.service.design.strategy.payment.bank.manager.WalletPaymentStrategy;
import lombok.RequiredArgsConstructor;

/**
 * Фабрика для создания стратегий оплаты.
 * Реализует паттерн "Фабрика" для инкапсуляции логики создания конкретных реализаций
 * {@link PaymentStrategy} в зависимости от типа платежа.
 * Поддерживаемые типы платежей:
 * {@link CardPaymentStrategy} (оплата картой)
 * {@link BankPaymentStrategy} (банковский перевод)
 * {@link WalletPaymentStrategy} (оплата через электронный кошелек)
 */
@RequiredArgsConstructor
public class PaymentStrategyFactory {

    private final CardPaymentStrategy cardPaymentStrategy;
    private final BankPaymentStrategy bankPaymentStrategy;
    private final WalletPaymentStrategy walletPaymentStrategy;

    /**
     * Создает и возвращает стратегию оплаты для указанного типа платежа.
     *
     * @param paymentType тип платежа (CARD, BANK, WALLET)
     * @return соответствующая реализация {@link PaymentStrategy}
     */
    public PaymentStrategy getPaymentStrategy(String paymentType) {
        return switch (paymentType) {
            case "CARD" -> cardPaymentStrategy;
            case "BANK" -> bankPaymentStrategy;
            case "WALLET" -> walletPaymentStrategy;
            default -> throw new IllegalArgumentException("Unsupported payment type: " + paymentType);
        };
    }
}