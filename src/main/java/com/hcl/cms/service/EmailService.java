package com.hcl.cms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // For injecting properties
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService { 
	private static final Logger log = LoggerFactory.getLogger(EmailService.class);
   
	@Autowired
	private JavaMailSender mailSender;

    // Injects the frontend URL from application.properties
    @Value("${frontend.reset-password.url}")
    private String frontendResetPasswordUrl;

    public void sendPasswordResetEmail(String toEmail, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true for multipart message (HTML)

        helper.setTo(toEmail);
        helper.setSubject("Password Reset Request for Your App");

        // Construct the full reset link using the frontend URL
        String resetLink = frontendResetPasswordUrl + "?token=" + token;

        String emailContent = "Hello,<br><br>"
                            + "You have requested to reset your password. Please click on the link below to reset your password:<br>"
                            + "<a href=\"" + resetLink + "\"><strong>Reset Password Link</strong></a><br><br>"
                            + "This link will expire in 24 hours. If you did not request a password reset, please ignore this email.<br><br>"
                            + "Regards,<br>"
                            + "The Your App Team";

        helper.setText(emailContent, true); // true indicates HTML content

        mailSender.send(message);
        log.info("Password reset email sent to {}", toEmail);
    }
}
