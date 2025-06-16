package com.hcl.cms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.hcl.cms.dto.ContactUsRequest;
import com.hcl.cms.dto.MemberLoginRequest;
import com.hcl.cms.dto.MemberLoginResponse;
import com.hcl.cms.dto.MemberRequest;
import com.hcl.cms.dto.MemberUpdateRequest;
import com.hcl.cms.entity.ContactUs;
import com.hcl.cms.entity.Member;
import com.hcl.cms.service.MemberService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/members")
//@CrossOrigin(origins = "http://localhost:3000")
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	@Autowired
	private MemberService memberService;

	@PostMapping("/register")
	public ResponseEntity<String> register(@Valid @RequestBody MemberRequest request) {
		logger.info("Received registration request for member: {}", request.getMemberId());
		String message = memberService.registerMember(request);
		logger.info("Registration completed: {}", message);
		return ResponseEntity.status(HttpStatus.CREATED).body(message);
	}

	@PostMapping("/login")
	public ResponseEntity<MemberLoginResponse> login(@Valid @RequestBody MemberLoginRequest request, HttpSession session) {
		logger.info("Login attempt for member: {}", request.getMemberId());
		String token = memberService.login(request, session);
		logger.info("Login successful, token generated.");
		return ResponseEntity.ok(new MemberLoginResponse(token));
	}

	@SecurityRequirement(name = "bearerAuth")
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpSession session) {
		logger.info("Logout request received.");
		session.invalidate();
		logger.info("Session invalidated successfully.");
		return ResponseEntity.ok("Logout Successful");
	}

	@SecurityRequirement(name = "bearerAuth")
	@PutMapping("/update")
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<Member> updateMemberDetails(
			@RequestHeader("Authorization") String token,
			@Valid @RequestBody MemberUpdateRequest request) {
		logger.info("Received request to update member details.");
		Member updated = memberService.updateMember(token, request);
		logger.info("Member updated successfully. ID: {}", updated.getId());
		return ResponseEntity.ok(updated);
	}
	
	@SecurityRequirement(name = "bearerAuth")
	@PostMapping("/contactUs")
	@PreAuthorize("hasRole('MEMBER')")
	public ResponseEntity<String> contactUs(@RequestHeader("Authorization") String token,@RequestBody ContactUsRequest request){
		logger.info("Received request to Save the ContactUS Details.");
		String contactUs=memberService.contactUs(token,request);
		logger.info("Save the details");
		return ResponseEntity.ok(contactUs);
	}

	// Optional helper method to avoid logging full JWTs
	private String maskToken(String token) {
		if (token == null || token.length() < 10) return "InvalidToken";
		return token.substring(0, 10) + "...";
	}
}
