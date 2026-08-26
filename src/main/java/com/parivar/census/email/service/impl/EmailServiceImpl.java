package com.parivar.census.email.service.impl;

import com.parivar.census.email.service.EmailService;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl
        implements EmailService {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    EmailServiceImpl.class
            );

    private final JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void sendPasswordResetEmail(
            String toEmail,
            String resetLink
    ) {

        try {

            logger.info(
                    "Sending password reset email to: {}",
                    toEmail
            );


            SimpleMailMessage message =
                    new SimpleMailMessage();


            message.setFrom(fromEmail);

            message.setTo(toEmail);

            message.setSubject(
                    "Password Reset - Shringirishi Census System"
            );

            message.setText(
                    "Hello,\n\n" +

                            "We received a request to reset your password " +
                            "for the Shringirishi Census System.\n\n" +

                            "Please click the link below to reset your password:\n\n" +

                            resetLink +

                            "\n\nThis link will expire in 15 minutes.\n\n" +

                            "If you did not request a password reset, " +
                            "please ignore this email.\n\n" +

                            "Regards,\n" +
                            "Shringirishi Census System"
            );


            mailSender.send(message);


            logger.info(
                    "Password reset email sent successfully to: {}",
                    toEmail
            );

        } catch (Exception exception) {

            logger.error(
                    "Failed to send password reset email to: {}",
                    toEmail,
                    exception
            );

            throw new RuntimeException(
                    "Failed to send password reset email"
            );
        }

    }

}