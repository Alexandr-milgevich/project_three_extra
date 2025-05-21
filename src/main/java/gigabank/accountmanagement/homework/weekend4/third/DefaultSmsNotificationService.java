package gigabank.accountmanagement.homework.weekend4.third;

import org.springframework.stereotype.Service;

@Service("defaultSmsNotificationService")
public class DefaultSmsNotificationService implements SmsNotificationService {
    @Override
    public void sendSms(String phoneNumber, String message) {
        System.out.println("SMS к " + phoneNumber + ": " + message);
    }
}
