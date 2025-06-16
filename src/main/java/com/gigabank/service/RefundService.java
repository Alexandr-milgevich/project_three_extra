package com.gigabank.service;

import com.gigabank.models.dto.response.RefundResponseDto;
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
     * @param refundResponseDto объект возврата, содержащий информацию о транзакции.
     */
    public void createRefund(RefundResponseDto refundResponseDto) {
        PaymentGatewayService paymentGatewayService = PaymentGatewayService.getInstance();
        paymentGatewayService.refund(refundResponseDto.getDescription(), refundResponseDto.getAmount());
    }
}