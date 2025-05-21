package gigabank.accountmanagement.service.notification;

import gigabank.accountmanagement.models.dto.UserDto;
import gigabank.accountmanagement.service.notification.adapter.EmailNotificationServiceAdapter;
import gigabank.accountmanagement.service.notification.adapter.SmsNotificationServiceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Адаптер для отправки уведомлений пользователям через SMS и email.
 * <p>
 * Этот класс предоставляет методы для отправки уведомлений пользователю через разные каналы:
 * SMS и email. Он использует соответствующие адаптеры {@link SmsNotificationServiceAdapter}
 * и {@link EmailNotificationServiceAdapter} для делегирования фактической отправки уведомлений.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class NotificationAdapter {

    private final SmsNotificationServiceAdapter smsNotificationServiceAdapter;
    private final EmailNotificationServiceAdapter emailNotificationServiceAdapter;

    /**
     * Отправляет все доступные уведомления пользователю (SMS и email).
     * <p>
     * Использует адаптеры для отправки как SMS-сообщений, так и email-сообщений.
     * </p>
     *
     * @param userDto      объект {@link UserDto}, содержащий контактные данные пользователя (номер телефона и email)
     * @param smsMessage   текст SMS-сообщения
     * @param emailSubject тема email-уведомления
     * @param emailBody    содержимое email-уведомления
     */
    public void sendAllNotificationToUser(UserDto userDto, String smsMessage, String emailSubject, String emailBody) {
        smsNotificationServiceAdapter.sendSms(userDto.getPhoneNumber(), smsMessage);
        emailNotificationServiceAdapter.sendEmail(userDto.getEmail(), emailSubject, emailBody);
    }

    /**
     * Отправляет только SMS-уведомление пользователю.
     *
     * @param userDto    объект {@link UserDto}, содержащий контактные данные пользователя
     * @param smsMessage текст SMS-сообщения
     */
    public void sendSmsNotificationToUser(UserDto userDto, String smsMessage) {
        smsNotificationServiceAdapter.sendSms(userDto.getPhoneNumber(), smsMessage);

    }

    /**
     * Отправляет только email-уведомление пользователю.
     *
     * @param userDto      объект {@link UserDto}, содержащий контактные данные пользователя
     * @param emailSubject тема email-уведомления
     * @param emailBody    содержимое email-уведомления
     */
    public void sendEmailNotificationToUser(UserDto userDto, String emailSubject, String emailBody) {
        emailNotificationServiceAdapter.sendEmail(userDto.getEmail(), emailSubject, emailBody);
    }
}
