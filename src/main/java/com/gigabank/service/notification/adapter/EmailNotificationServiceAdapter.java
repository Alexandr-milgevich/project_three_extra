package com.gigabank.service.notification.adapter;

/**
 * Интерфейс для адаптера, отправляющего email-уведомления.
 * <p>
 * Этот интерфейс определяет контракт для отправки электронных писем. Реализация этого интерфейса
 * должна обеспечить отправку email-сообщений на указанный адрес электронной почты с заданной темой и содержимым.
 * </p>
 */
public interface EmailNotificationServiceAdapter {

    /**
     * Отправляет email-сообщение на указанный адрес электронной почты.
     *
     * @param email   Адрес электронной почты получателя.
     * @param subject Тема письма.
     * @param body    Содержимое письма.
     */
    void sendEmail(String email, String subject, String body);
}
