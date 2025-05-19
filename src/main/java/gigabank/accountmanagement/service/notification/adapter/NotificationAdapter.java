package gigabank.accountmanagement.service.notification.adapter;

import gigabank.accountmanagement.models.dto.UserDto;
import gigabank.accountmanagement.service.notification.ExternalNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Реализация адаптера для отправки уведомлений через внешний сервис.
 * Обеспечивает взаимодействие между системой и {@link ExternalNotificationService}.
 * <p>
 * Использует композицию для делегирования отправки уведомлений внешнему сервису.
 * Поддерживает как прямую отправку по SMS/email, так и отправку через объект {@link UserDto}.
 */
@Component
@RequiredArgsConstructor
public class NotificationAdapter implements NotificationServiceAdapter {
    private final ExternalNotificationService externalNotificationService;

    /**
     * {@inheritDoc}
     *
     * @implNote Делегирует вызов методу {@link ExternalNotificationService#sendSms(String, String)}
     */
    @Override
    public void sendSms(String phone, String message) {
        externalNotificationService.sendSms(phone, message);
    }

    /**
     * {@inheritDoc}
     *
     * @implNote Делегирует вызов методу {@link ExternalNotificationService#sendEmail(String, String, String)}
     */
    @Override
    public void sendEmail(String email, String subject, String body) {
        externalNotificationService.sendEmail(email, subject, body);
    }

    /**
     * Отправляет комбинированное уведомление пользователю через все доступные каналы.
     *
     * @param userDto      объект {@link UserDto}, содержащий контактные данные (телефон и email)
     * @param smsMessage   текст SMS-сообщения
     * @param emailSubject тема email-уведомления
     * @param emailBody    содержимое email-уведомления
     */
    public void sendNotificationToUser(UserDto userDto, String smsMessage, String emailSubject, String emailBody) {
        sendSms(userDto.getPhoneNumber(), smsMessage);
        sendEmail(userDto.getEmail(), emailSubject, emailBody);
    }
}