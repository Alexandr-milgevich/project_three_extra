package com.gigabank.service.notification;

import com.gigabank.service.notification.adapter.EmailNotificationServiceAdapter;
import com.gigabank.service.notification.adapter.SmsNotificationServiceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Адаптер для отправки уведомлений пользователям через SMS и email.
 * Этот класс предоставляет методы для отправки уведомлений пользователю через разные каналы:
 * SMS и email. Он использует соответствующие адаптеры {@link SmsNotificationServiceAdapter}
 * и {@link EmailNotificationServiceAdapter} для делегирования фактической отправки уведомлений.
 */
@Service
@RequiredArgsConstructor
public class NotificationAdapter {

    private final SmsNotificationServiceAdapter smsNotificationServiceAdapter;
    private final EmailNotificationServiceAdapter emailNotificationServiceAdapter;

    /**
     * Отправляет все доступные уведомления пользователю (SMS и email).
     * Использует адаптеры для отправки как SMS-сообщений, так и email-сообщений.
     *
     * @param phoneNumber  номер телефона пользователя
     * @param email        эл.почта пользователя
     * @param smsMessage   текст SMS-сообщения
     * @param emailSubject тема email-уведомления
     * @param emailBody    содержимое email-уведомления
     */
    public void sendAllNotificationToUser(String phoneNumber, String email, String smsMessage, String emailSubject,
                                          String emailBody) {
        smsNotificationServiceAdapter.sendSms(phoneNumber, smsMessage);
        emailNotificationServiceAdapter.sendEmail(email, emailSubject, emailBody);
    }

    /**
     * Отправляет только SMS-уведомление пользователю.
     *
     * @param phoneNumber номер телефона пользователя
     * @param smsMessage  текст SMS-сообщения
     */
    public void sendSmsNotificationToUser(String phoneNumber, String smsMessage) {
        smsNotificationServiceAdapter.sendSms(phoneNumber, smsMessage);

    }

    /**
     * Отправляет только email-уведомление пользователю.
     *
     * @param email        эл.почта пользователя
     * @param emailSubject тема email-уведомления
     * @param emailBody    содержимое email-уведомления
     */
    public void sendEmailNotificationToUser(String email, String emailSubject, String emailBody) {
        emailNotificationServiceAdapter.sendEmail(email, emailSubject, emailBody);
    }
}