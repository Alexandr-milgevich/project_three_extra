package gigabank.accountmanagement.service.bank.service.factory;

import gigabank.accountmanagement.constants.PaymentType;
import gigabank.accountmanagement.service.bank.service.strategy.BankTransferServiceStrategy;
import gigabank.accountmanagement.service.bank.service.strategy.CardPaymentServiceStrategy;
import gigabank.accountmanagement.service.bank.service.strategy.DigitalWalletServiceStrategy;
import gigabank.accountmanagement.service.bank.service.strategy.PaymentServiceStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Фабрика для создания стратегий обработки платежей.
 * <p>
 * Реализует паттерн "Фабричный метод" для предоставления соответствующей реализации
 * {@link PaymentServiceStrategy} в зависимости от типа платежа. Инкапсулирует логику
 * создания объектов стратегий и обеспечивает гибкость при добавлении новых типов платежей.
 * </p>
 *
 * <p><b>Поддерживаемые стратегии:</b>
 * <li>{@link CardPaymentServiceStrategy} - обработка платежей по банковским картам</li>
 * <li>{@link BankTransferServiceStrategy} - обработка банковских переводов</li>
 * <li>{@link DigitalWalletServiceStrategy} - обработка платежей через электронные кошельки</li>
 * </p>
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceStrategyFactory {

    private final CardPaymentServiceStrategy cardPaymentStrategy;
    private final BankTransferServiceStrategy bankPaymentStrategy;
    private final DigitalWalletServiceStrategy walletPaymentStrategy;

    /**
     * Возвращает стратегию обработки платежа для указанного типа.
     *
     * @param paymentType тип платежа из перечисления {@link PaymentType}
     * @return соответствующая реализация интерфейса {@link PaymentServiceStrategy}
     */
    public PaymentServiceStrategy getPaymentStrategy(PaymentType paymentType) {
        return switch (paymentType) {
            case CARD -> cardPaymentStrategy;
            case BANK -> bankPaymentStrategy;
            case WALLET -> walletPaymentStrategy;
            default -> throw new IllegalArgumentException("Unsupported payment type: " + paymentType);
        };
    }
}