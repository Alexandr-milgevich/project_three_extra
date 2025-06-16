package com.gigabank.homework.weekend4.third;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("defaultEmailNotificationService")
public class DefaultEmailNotificationService implements EmailNotificationService {

    @Value("${email.sender}")
    private String senderEmail;

    @Override
    public void sendEmail(String to, String subject, String body) {
        System.out.println("Email от " + senderEmail + " к " + to + ": " + subject + " - " + body);
    }
}
