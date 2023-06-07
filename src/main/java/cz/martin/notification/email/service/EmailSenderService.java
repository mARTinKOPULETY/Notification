package cz.martin.notification.email.service;

import jakarta.mail.MessagingException;

public interface EmailSenderService {
    void sendEmail() throws MessagingException;
}
