package com.example.MovieJPA.service;

import com.example.MovieJPA.exception.EmailSedingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sentEmail(String to, String subject, String body) throws EmailSedingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        try {
            javaMailSender.send(message);
        }catch (MailException e){
            throw new EmailSedingException("Failed to send email: " + e.getMessage());
        }

    }
}
