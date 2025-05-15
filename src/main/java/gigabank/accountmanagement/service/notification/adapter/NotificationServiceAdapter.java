package gigabank.accountmanagement.service.notification.adapter;

public interface NotificationServiceAdapter {
    void sendSms(String phone, String message);

    void sendEmail(String email, String subject, String body);
}
