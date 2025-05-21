package gigabank.accountmanagement.service.bank.manager.factory;

import gigabank.accountmanagement.constants.PaymentType;
import gigabank.accountmanagement.service.bank.manager.strategy.BankPaymentManagerStrategy;
import gigabank.accountmanagement.service.bank.manager.strategy.CardPaymentManagerStrategy;
import gigabank.accountmanagement.service.bank.manager.strategy.DigitalWalletPaymentManagerStrategy;
import gigabank.accountmanagement.service.bank.manager.strategy.PaymentManagerStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Фабрика для создания стратегий обработки платежей.
 * <p>
 * Реализует паттерн "Фабричный метод" для предоставления соответствующей реализации
 * {@link PaymentManagerStrategy} в зависимости от типа платежа. Инкапсулирует логику
 * создания объектов стратегий и обеспечивает гибкость при добавлении новых типов платежей.
 * </p>
 *
 * <p><b>Поддерживаемые стратегии:</b>
 * <li>{@link CardPaymentManagerStrategy} - обработка платежей по банковским картам</li>
 * <li>{@link BankPaymentManagerStrategy} - обработка банковских переводов</li>
 * <li>{@link DigitalWalletPaymentManagerStrategy} - обработка платежей через электронные кошельки</li>
 * </p>
 */
@Service
@RequiredArgsConstructor
public class PaymentManagerStrategyFactory {

    private final CardPaymentManagerStrategy cardPaymentStrategy;
    private final BankPaymentManagerStrategy bankPaymentStrategy;
    private final DigitalWalletPaymentManagerStrategy walletPaymentStrategy;

    /**
     * Возвращает стратегию обработки платежа для указанного типа.
     *
     * @param paymentType тип платежа из перечисления {@link PaymentType}
     * @return соответствующая реализация интерфейса {@link PaymentManagerStrategy}
     */
    public PaymentManagerStrategy getPaymentStrategy(PaymentType paymentType) {
        return switch (paymentType) {
            case CARD -> cardPaymentStrategy;
            case BANK -> bankPaymentStrategy;
            case WALLET -> walletPaymentStrategy;
            default -> throw new IllegalArgumentException("Unsupported payment type: " + paymentType);
        };
    }
}