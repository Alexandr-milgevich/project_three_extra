package gigabank.accountmanagement.homework.weekend4.third;

public interface EmailNotificationService {
    void sendEmail(String to, String subject, String body);
}