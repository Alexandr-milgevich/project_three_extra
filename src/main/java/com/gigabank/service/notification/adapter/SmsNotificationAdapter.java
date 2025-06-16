package com.gigabank.service.notification.adapter;

import com.gigabank.service.notification.SmsNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Адаптер для отправки SMS-уведомлений через {@link SmsNotificationService}.
 * <p>
 * Реализует интерфейс {@link SmsNotificationServiceAdapter}, предоставляя метод для
 * отправки SMS-сообщений. Делегирует вызовы внешнему сервису {@link SmsNotificationService}.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class SmsNotificationAdapter implements SmsNotificationServiceAdapter {

    private final SmsNotificationService smsNotificationService;

    /**
     * Отправляет SMS-сообщение на указанный номер телефона.
     *
     * @param phone   Номер телефона получателя.
     * @param message Текст SMS-сообщения.
     * @implNote Метод делегирует вызов в {@link SmsNotificationService#sendSms(String, String)}.
     */
    @Override
    public void sendSms(String phone, String message) {
        smsNotificationService.sendSms(phone, message);
    }
}