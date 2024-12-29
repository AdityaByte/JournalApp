package com.journalapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {
    
    @Autowired
    private EmailService emailService;

    @Test
    public void sendEmailTest(){
        assertTrue(emailService.sendEmail("cybergeek563@gmail.com", "Sending email using java", "Hi aditya what you doing"));
    }

    @Test
    public void checkEnv(){
        System.out.println(System.getenv("EMAIL"));
        assertEquals("adityabyte@gmail.com", System.getenv("EMAIL"));
    }

}
