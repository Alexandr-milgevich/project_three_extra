package gigabank.accountmanagement.service.notification.adapter;

import gigabank.accountmanagement.entity.User;
import gigabank.accountmanagement.service.notification.ExternalNotificationService;

public class NotificationAdapter implements NotificationServiceAdapter {
    private final ExternalNotificationService externalNotificationService;

    public NotificationAdapter(ExternalNotificationService externalNotificationService) {
        this.externalNotificationService = externalNotificationService;
    }

    @Override
    public void sendSms(String phone, String message) {
        externalNotificationService.sendSms(phone, message);
    }

    @Override
    public void sendEmail(String email, String subject, String body) {
        externalNotificationService.sendEmail(email, subject, body);
    }

    // Метод для отправки уведомлений пользователю через объект User
    public void sendNotificationToUser(User user, String smsMessage, String emailSubject, String emailBody) {
        sendSms(user.getPhoneNumber(), smsMessage);
        sendEmail(user.getEmail(), emailSubject, emailBody);
    }
}