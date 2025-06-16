package com.hcl.cms.service;

import jakarta.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // For encoding new password
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // For atomicity

import com.hcl.cms.entity.Member;
import com.hcl.cms.entity.PasswordResetToken;
import com.hcl.cms.repository.MemberRepository;
import com.hcl.cms.repository.PasswordResetTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
	private static final Logger log = LoggerFactory.getLogger(AuthService.class);

 @Autowired
 private MemberRepository memberRepository;

 @Autowired
 private PasswordResetTokenRepository passwordResetTokenRepository;

 @Autowired
 private EmailService emailService;

 @Autowired
 private PasswordEncoder passwordEncoder; // Injected to encode passwords

 // Exception for when a member is not found (optional, but good practice)
 public static class MemberNotFoundException extends RuntimeException {
     public MemberNotFoundException(String message) {
         super(message);
     }
 }

 /**
  * Initiates the password reset process by generating a token and sending an email.
  * @param email The email of the member requesting the reset.
  * @throws MessagingException if there's an issue sending the email.
  */
 @Transactional
 public void initiatePasswordReset(String email) throws MessagingException {
     Member member = memberRepository.findByEmail(email)
             .orElse(null); // Return null if not found for security purposes

     // IMPORTANT: Always return a generic success message to prevent email enumeration.
     if (member == null) {
         log.warn("Password reset requested for non-existent email: {}", email);
         // We return here but could also throw a specific exception if business rules require it,
         // then the controller would catch it and still return a generic success message.
         return;
     }

     // Clean up any existing tokens for this member to ensure only one is active
     passwordResetTokenRepository.deleteByMember(member);

     // Generate a new unique, secure token
     String token = UUID.randomUUID().toString();
     // Set expiry date (e.g., 24 hours from now)
     LocalDateTime expiryDate = LocalDateTime.now().plusHours(24);

     // Create and save the new password reset token linked to the member
     PasswordResetToken resetToken = new PasswordResetToken(token, member, expiryDate);
     passwordResetTokenRepository.save(resetToken);

     // Send the password reset email
     emailService.sendPasswordResetEmail(member.getEmail(), token);
 }

 /**
  * Resets a member's password using a valid token.
  * @param token The password reset token from the URL.
  * @param newPassword The new password provided by the member (plain text, will be encoded).
  * @return true if the password was successfully reset, false otherwise (e.g., invalid/expired token).
  */
 @Transactional
 public boolean resetPassword(String token, String newPassword) {
     PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
             .orElse(null);

     // Check if token exists and is not expired
     if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
         log.warn("Attempted password reset with invalid or expired token: {}", token);
         return false;
     }

     Member member = resetToken.getMember();

     // Encode the new password using the configured PasswordEncoder
     String encodedNewPassword = passwordEncoder.encode(newPassword);
     member.setPassword(encodedNewPassword); // Update the member's password
     memberRepository.save(member); // Save the updated member to the database

     // Invalidate the token after use by deleting it
     passwordResetTokenRepository.delete(resetToken);
     log.info("Password successfully reset for member with email: {}", member.getEmail());
     return true;
 }
}
