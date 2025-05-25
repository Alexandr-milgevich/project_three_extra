package com.gigabank.service.notification;

import org.springframework.stereotype.Service;

/**
 * Сервис для отправки SMS-сообщений.
 * <p>
 * Этот класс предоставляет метод для отправки SMS-сообщений на указанный номер телефона.
 * </p>
 */
@Service
public class SmsNotificationService {

    /**
     * Отправляет SMS-сообщение на указанный номер телефона.
     * <p>
     * Метод эмулирует отправку SMS-сообщения, выводя информацию о сообщении в консоль.
     * </p>
     *
     * @param phone Номер телефона получателя в международном или национальном формате.
     * @param msg   Текст сообщения для отправки.
     */
    public void sendSms(String phone, String msg) {
        System.out.println("Sending SMS to " + phone + ": " + msg);
    }
}
