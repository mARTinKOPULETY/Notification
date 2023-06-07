package cz.martin.notification.email.service.impl;

import cz.martin.notification.email.service.EmailSenderService;
import cz.martin.notification.email.template.EmailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender javaMailSender;

    public void sendEmail() throws MessagingException, MessagingException {
        EmailMessage emailMessage = new EmailMessage();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(emailMessage.getTo());
        helper.setSubject(emailMessage.getSubject());

        // Creating HTML form of message
        String htmlMessage = "<h3>" + emailMessage.getMessage() + "</h3>" +
                "<h3>" + emailMessage.getLink() +"</h3>";

        helper.setText(htmlMessage, true);

        javaMailSender.send(mimeMessage);
    }
}
