package com.gigabank.service.notification.adapter;

/**
 * Интерфейс для адаптера, отправляющего SMS-уведомления.
 * <p>
 * Этот интерфейс определяет контракт для отправки SMS-сообщений. Реализация этого интерфейса
 * должна обеспечить отправку SMS-сообщений на указанный номер телефона с заданным текстом.
 * </p>
 */
public interface SmsNotificationServiceAdapter {

    /**
     * Отправляет SMS-сообщение на указанный номер телефона.
     *
     * @param phone   Номер телефона получателя.
     * @param message Текст SMS-сообщения.
     */
    void sendSms(String phone, String message);
}