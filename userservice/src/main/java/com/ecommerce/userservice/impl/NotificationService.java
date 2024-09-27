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
            message.setSubject("Welcome to ShoppingTime - Your Registration is Successful!");
            message.setText(
                    "Hi there,\n\n" +
                    "Thank you for signing up at ShoppingTime! We are excited to have you on board. " +
                    "Your account has been successfully created with the email ID: " + email + ".\n\n" +
                    "What's next?\n" +
                    "You can now explore a wide variety of products, add items to your cart, and shop at your convenience. " +
                    "Don't forget to check out our latest offers and deals tailored just for you!\n\n" +
                    "To get started, simply log in with your email and password and start shopping.\n\n" +
                    "If you have any questions or need support, feel free to reach out to our customer service team anytime at support@shoppingtime.com.\n\n" +
                    "Happy Shopping!\n" +
                    "The ShoppingTime Team\n\n" +
                    "--------------------------------------------\n" +
                    "ShoppingTime - Your one-stop destination for all your shopping needs!"
            );

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
