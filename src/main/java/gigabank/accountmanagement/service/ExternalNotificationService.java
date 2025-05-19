package gigabank.accountmanagement.service;

import org.springframework.stereotype.Service;

/**
 * Сервис для отправки уведомлений через внешний API.
 * Эмулирует работу внешнего сервиса отправки SMS и email-уведомлений.
 */
@Service
public class ExternalNotificationService {

    /**
     * Отправляет SMS-сообщение на указанный телефонный номер.
     *
     * @param phone Номер телефона получателя.
     * @param msg   Текст SMS-сообщения.
     */
    public void sendSms(String phone, String msg) {
        System.out.println("Отправка SMS на " + phone + ": " + msg);
    }

    /**
     * Отправляет email-сообщение на указанный адрес электронной почты.
     *
     * @param email   Адрес электронной почты получателя.
     * @param subject Тема письма.
     * @param body    Содержимое письма.
     */
    public void sendEmail(String email, String subject, String body) {
        System.out.println("Отправка Email на " + email + ": " + subject + " - " + body);
    }
}