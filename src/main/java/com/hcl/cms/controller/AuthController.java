package com.hcl.cms.controller;

import jakarta.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hcl.cms.dto.ForgotPasswordRequest;
import com.hcl.cms.dto.ResetPasswordRequest;
import com.hcl.cms.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
 @Autowired
 private AuthService authService;

 /**
  * Handles the "Forgot Password" request.
  * Receives an email and sends a password reset link if the email exists.
  * Returns a generic success message for security reasons.
  * @param request The ForgotPasswordRequest containing the member's email.
  * @return ResponseEntity with a success message or an error.
  */
 @PostMapping("/forgot-password")
 public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
     if (request.getEmail() == null || request.getEmail().isEmpty()) {
         return ResponseEntity.badRequest().body("Email is required.");
     }

     try {
         authService.initiatePasswordReset(request.getEmail());
         // Always return a generic success message for security to prevent email enumeration.
         return ResponseEntity.ok("If an account exists with that email, a password reset link has been sent.");
     } catch (MessagingException e) {
         // Log the actual error for debugging, but return a generic error to the frontend.
         log.error("Failed to send password reset email for {}: {}", request.getEmail(), e.getMessage());
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                              .body("Error sending password reset email. Please try again later.");
     } catch (Exception e) {
         log.error("An unexpected error occurred during forgot password initiation for {}: {}", request.getEmail(), e.getMessage());
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                              .body("An unexpected error occurred. Please try again.");
     }
 }

 /**
  * Handles the "Reset Password" request.
  * Receives a token from the URL and the new password from the request body.
  * Validates the token and updates the member's password.
  * @param token The password reset token from the URL query parameter.
  * @param resetRequest The ResetPasswordRequest containing the new password.
  * @return ResponseEntity with a success or error message.
  */
 @PostMapping("/reset-password")
 public ResponseEntity<String> resetPassword(
         @RequestParam("token") String token, // Token from URL query parameter
         @RequestBody ResetPasswordRequest resetRequest) { // New password from request body

     if (token == null || token.isEmpty()) {
         return ResponseEntity.badRequest().body("Password reset token is missing.");
     }
     if (resetRequest.getNewPassword() == null || resetRequest.getNewPassword().isEmpty()) {
         return ResponseEntity.badRequest().body("New password is required.");
     }
     // Add more robust password validation rules here (e.g., min length, complexity)
     if (resetRequest.getNewPassword().length() < 6) {
         return ResponseEntity.badRequest().body("New password must be at least 6 characters long.");
     }

     try {
         boolean success = authService.resetPassword(token, resetRequest.getNewPassword());

         if (success) {
             return ResponseEntity.ok("Password has been reset successfully.");
         } else {
             // This covers invalid or expired tokens
             return ResponseEntity.badRequest().body("Invalid or expired password reset token. Please request a new one.");
         }
     } catch (Exception e) {
         log.error("An error occurred during password reset for token {}: {}", token, e.getMessage());
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                              .body("An unexpected error occurred during password reset. Please try again.");
     }
 }
}