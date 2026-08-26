package com.parivar.census.email.service;

public interface EmailService {

    void sendPasswordResetEmail(
            String toEmail,
            String resetLink
    );

}