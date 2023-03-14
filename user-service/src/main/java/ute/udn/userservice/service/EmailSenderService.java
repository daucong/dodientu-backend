package ute.udn.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendMail(String userEmail, String confirmationToken) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(userEmail);
        mailMessage.setSubject("Account Activation!");
        mailMessage.setText("Here token for reset password: " + confirmationToken
                + "   Note: This link will expire after 10 minutes.");
        javaMailSender.send(mailMessage);
    }

    public boolean sendSimpleMail(String to, String sub, String body) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(sender);
        mailMessage.setTo(to);
        mailMessage.setSubject(sub);
        mailMessage.setText(body);
        Boolean isSent = false;
        try {
            javaMailSender.send(mailMessage);
            isSent = true;
        } catch (Exception e) {
        }

        return isSent;
    }
}
