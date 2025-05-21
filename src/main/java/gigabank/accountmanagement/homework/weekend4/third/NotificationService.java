package gigabank.accountmanagement.homework.weekend4.third;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Qualifier("defaultEmailNotificationService")
    private final EmailNotificationService emailNotificationService;

    @Qualifier("defaultSmsNotificationService")
    private final SmsNotificationService smsNotificationService;

    public void sendSms(String phone, String msg) {
        smsNotificationService.sendSms(phone, msg);
    }

    public void sendEmail(String email, String subject, String body) {
        emailNotificationService.sendEmail(email, subject, body);
    }
}