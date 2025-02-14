package com.assurance.demo.web.service;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    /*
    *
    * @param to String
    * @param subject String
    * @param text String
    * @throws MessagingException
    * */
    public void sendEmail(String to, String subject, String text) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = multipart message
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); // true means the text is HTML

        // Set the from address (you can configure this in your application.properties)
        helper.setFrom("aminejabeur99@gmail.com");

        // Send the email
        javaMailSender.send(message);
    }
}