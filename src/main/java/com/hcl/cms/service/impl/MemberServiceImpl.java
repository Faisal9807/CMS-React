package com.hcl.cms.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hcl.cms.dto.ContactUsRequest;
import com.hcl.cms.dto.MemberLoginRequest;
import com.hcl.cms.dto.MemberRequest;
import com.hcl.cms.dto.MemberUpdateRequest;
import com.hcl.cms.entity.ContactUs;
import com.hcl.cms.entity.Member;
import com.hcl.cms.enums.MemberStatus;
import com.hcl.cms.exeption.ResourceAlreadyExistsException;
import com.hcl.cms.exeption.ResourceNotFoundException;
import com.hcl.cms.repository.ContactUsRepository;
import com.hcl.cms.repository.MemberRepository;
import com.hcl.cms.security.JwtUtil;
import com.hcl.cms.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Service
public class MemberServiceImpl implements MemberService {

	private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private ContactUsRepository contactUsRepository;

	@Autowired
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public String registerMember(MemberRequest request) {
		logger.info("Attempting to register member with ID: {}", request.getMemberId());

		if (memberRepository.findByMemberId(request.getMemberId()).isPresent()) {
			logger.warn("Registration failed: Member ID {} already exists", request.getMemberId());
			throw new ResourceAlreadyExistsException("Member ID already registered");
		}

		Member member = new Member();
		member.setFirstName(request.getFirstName());
		member.setLastName(request.getLastName());
		member.setAge(request.getAge());
		member.setEmail(request.getEmail());
		member.setGender(request.getGender());
		member.setContactNumber(request.getContactNumber());
		member.setMemberId(request.getMemberId());
		member.setPassword(encoder.encode(request.getPassword()));
		member.setApproved(false);

		memberRepository.save(member);
		logger.info("Member with ID {} registered successfully", request.getMemberId());

		return "Your details are submitted successfully";
	}

	@Override
	public String login(MemberLoginRequest request, HttpSession session) {
		logger.info("Login attempt for member ID: {}", request.getMemberId());

		Member member = memberRepository.findByMemberId(request.getMemberId())
				.orElseThrow(() -> {
					logger.error("Login failed: Member ID {} not found", request.getMemberId());
					return new ResourceNotFoundException("Member not found");
				});

		if (!encoder.matches(request.getPassword(), member.getPassword())) {
			logger.warn("Login failed: Invalid credentials for member ID: {}", request.getMemberId());
			throw new RuntimeException("Invalid credentials");
		}

		if (!member.getStatus().equals(MemberStatus.APPROVED)) {
			logger.warn("Login denied: Member ID {} not approved", request.getMemberId());
			throw new RuntimeException("Member not approved by admin");
		}

		session.setAttribute("memberId", member.getMemberId());
		String token = jwtUtil.generateToken(member, "ROLE_MEMBER");
		logger.info("Login successful for member ID: {}", request.getMemberId());
		return token;
	}

	@Override
	public Member updateMember(String token, MemberUpdateRequest dto) {
		String memberId = jwtUtil.extractUsername(token);
		logger.info("Update request for member ID: {}", memberId);

		Member member = memberRepository.findByMemberId(memberId)
				.orElseThrow(() -> {
					logger.error("Update failed: Member not found with ID: {}", memberId);
					return new ResourceNotFoundException("Member not found with ID: " + memberId);
				});

		member.setFirstName(dto.getFirstName());
		member.setLastName(dto.getLastName());
		member.setAge(dto.getAge());
		member.setGender(dto.getGender());
		member.setContactNumber(dto.getContactNumber());

		Member updatedMember = memberRepository.save(member);
		logger.info("Member profile updated successfully for ID: {}", memberId);

		return updatedMember;
	}

	@Override
	public String contactUs(String token, ContactUsRequest request) {
		String memberId = jwtUtil.extractUsername(token);
		ContactUs contactUs=new ContactUs();
		contactUs.setMemberId(memberId);
		contactUs.setName(request.getName());
		contactUs.setPhone(request.getPhone());
		contactUs.setEmal(request.getEmail());
		contactUs.setMessage(request.getMessage());
		
		contactUsRepository.save(contactUs);
		
		return "Your details are submitted successfully";
	}

}
