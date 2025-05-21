package gigabank.accountmanagement.service.notification;

import org.springframework.stereotype.Service;

/**
 * Сервис для отправки email-уведомлений.
 * <p>
 * Этот сервис отвечает за отправку электронных писем с заданной темой и содержанием
 * на указанный адрес электронной почты. В данном примере метод отправки просто выводит
 * сообщение в консоль.
 * </p>
 */
@Service
public class EmailNotificationService {

    /**
     * Отправляет email-сообщение на указанный адрес электронной почты.
     *
     * @param email   Адрес электронной почты получателя.
     * @param subject Тема письма.
     * @param body    Содержимое письма.
     * @implNote Метод выводит информацию о отправке письма в консоль.
     */
    public void sendEmail(String email, String subject, String body) {
        System.out.println("Sending Email to " + email + ": " + subject + " - " + body);
    }
}
