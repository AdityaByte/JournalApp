package com.journalapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {
    
    @Autowired
    private JavaMailSender javaMailSender;

    public boolean sendEmail(String to, String subject, String text){
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            log.info(simpleMailMessage.getFrom());
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(text);
            javaMailSender.send(simpleMailMessage);
            log.info("email sent successfully");
            return true;
        }
        catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

}
