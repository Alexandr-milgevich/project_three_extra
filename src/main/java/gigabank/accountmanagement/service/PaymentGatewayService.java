package gigabank.accountmanagement.service;

import java.math.BigDecimal;

/**
 * Класс, представляющий внешнюю платежную систему.
 * Этот класс реализует паттерн Singleton,
 * обеспечивая создание только одного экземпляра подключения к платёжной системе.
 */
public class PaymentGatewayService {

    /**
     * Статическое поле для хранения единственного экземпляра синглтона
     */
    private static PaymentGatewayService paymentGatewayService = new PaymentGatewayService();

    /**
     * Получение единственного экземпляра класса.
     * Если экземпляр еще не был создан, создается новый.
     *
     * @return Экземпляр класса {@link PaymentGatewayService}.
     */
    public static synchronized PaymentGatewayService getInstance() {
        if (paymentGatewayService == null) {
            paymentGatewayService = new PaymentGatewayService();
            System.out.println("Создано новое подключение к платёжной системе...");
        }
        return paymentGatewayService;
    }

    /**
     * Конструктор класса {@link PaymentGatewayService}.
     * Создает подключение к платёжной системе при первом вызове.
     */
    private PaymentGatewayService() {
        System.out.println("Создано новое подключение к платёжной системе...");
    }

    /**
     * Эмулирует авторизацию транзакции в платёжной системе.
     *
     * @param txId   Идентификатор транзакции.
     * @param amount Сумма транзакции.
     * @return {@code true}, если авторизация прошла успешно.
     */
    public boolean authorize(String txId, BigDecimal amount) {
        //здесь эмуляция вызова внешней платежной системы
        System.out.println("Авторизация транзакции " + txId + " на сумму " + amount);
        return true;
    }

    /**
     * Эмулирует возврат транзакции в платёжной системе.
     *
     * @param txId   Идентификатор транзакции.
     * @param amount Сумма транзакции для возврата.
     * @return {@code true}, если возврат прошел успешно.
     */
    public boolean refund(String txId, BigDecimal amount) {
        //здесь эмуляция вызова внешней платежной системы
        System.out.println("Осуществление возврата " + txId + " на сумму " + amount);
        return true;
    }
}