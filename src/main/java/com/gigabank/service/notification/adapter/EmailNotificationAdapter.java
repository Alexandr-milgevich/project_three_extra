package com.gigabank.service.notification.adapter;

import com.gigabank.service.notification.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Адаптер для отправки email-уведомлений через {@link EmailNotificationService}.
 * <p>
 * Реализует интерфейс {@link EmailNotificationServiceAdapter}, предоставляя метод для
 * отправки email-сообщений. Делегирует вызовы внешнему сервису {@link EmailNotificationService}.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class EmailNotificationAdapter implements EmailNotificationServiceAdapter {

    private final EmailNotificationService emailNotificationService;

    /**
     * Отправляет email-сообщение на указанный адрес электронной почты.
     *
     * @param email   Адрес электронной почты получателя.
     * @param subject Тема письма.
     * @param body    Содержимое письма.
     * @implNote Метод делегирует вызов в {@link EmailNotificationService#sendEmail(String, String, String)}.
     */
    @Override
    public void sendEmail(String email, String subject, String body) {
        emailNotificationService.sendEmail(email, subject, body);
    }
}
