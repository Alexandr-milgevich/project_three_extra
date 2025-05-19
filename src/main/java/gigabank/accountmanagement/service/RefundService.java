package gigabank.accountmanagement.service;

import gigabank.accountmanagement.models.dto.Refund;
import org.springframework.stereotype.Service;

/**
 * Сервис, осуществляющий возвраты по платежам.
 * Использует синглтон {@link PaymentGatewayService} для взаимодействия с платёжной системой.
 */
@Service
public class RefundService {

    /**
     * Метод для создания возврата.
     * Получает данные возврата и вызывает метод возврата на единственном экземпляре {@link PaymentGatewayService}.
     *
     * @param refund объект возврата, содержащий информацию о транзакции.
     */
    public void createRefund(Refund refund) {
        PaymentGatewayService paymentGatewayService = PaymentGatewayService.getInstance();
        paymentGatewayService.refund(refund.getDescription(), refund.getAmount());
    }
}
