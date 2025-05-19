package gigabank.accountmanagement.design.adapter;

import gigabank.accountmanagement.models.dto.User;
import gigabank.accountmanagement.service.ExternalNotificationService;

/**
 * Реализация адаптера для отправки уведомлений через внешний сервис.
 * Обеспечивает взаимодействие между системой и {@link ExternalNotificationService}.
 * <p>
 * Использует композицию для делегирования отправки уведомлений внешнему сервису.
 * Поддерживает как прямую отправку по SMS/email, так и отправку через объект {@link User}.
 */
public class NotificationAdapter implements NotificationServiceAdapter {
    private final ExternalNotificationService externalNotificationService;

    /**
     * Конструктор с внедрением зависимости внешнего сервиса уведомлений.
     *
     * @param externalNotificationService экземпляр {@link ExternalNotificationService}
     *                                    для реальной отправки уведомлений
     */
    public NotificationAdapter(ExternalNotificationService externalNotificationService) {
        this.externalNotificationService = externalNotificationService;
    }

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
     * @param user         объект {@link User}, содержащий контактные данные (телефон и email)
     * @param smsMessage   текст SMS-сообщения
     * @param emailSubject тема email-уведомления
     * @param emailBody    содержимое email-уведомления
     */
    public void sendNotificationToUser(User user, String smsMessage, String emailSubject, String emailBody) {
        sendSms(user.getPhoneNumber(), smsMessage);
        sendEmail(user.getEmail(), emailSubject, emailBody);
    }
}