package com.ecommerce.userservice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String email) {
        try {
            // Create a simple mail message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Registration Successful");
            message.setText("Thank you for choosing our service! Your registration is complete with email ID: " + email + 
                            ". Now you can log in using your email and password.");

            // Send the email
            mailSender.send(message);
            System.out.println("Email sent to " + email);

        } catch (MailException e) {
            // Handle the exception in case email fails to send
            System.err.println("Error sending email: " + e.getMessage());
            // You could log this or rethrow it as a custom exception
        }
    }
}
