package com.hcl.cms.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hcl.cms.dto.AdminRequest;
import com.hcl.cms.dto.JwtResponse;
import com.hcl.cms.dto.MemberApprovalRequest;
import com.hcl.cms.dto.MemberDetailsResponse;
import com.hcl.cms.entity.Admin;
import com.hcl.cms.entity.Member;
import com.hcl.cms.enums.MemberStatus;
import com.hcl.cms.exeption.MemberAlreadyApprovedOrRejectException;
import com.hcl.cms.exeption.ResourceAlreadyExistsException;
import com.hcl.cms.exeption.ResourceNotFoundException;
import com.hcl.cms.repository.AdminRepository;
import com.hcl.cms.repository.MemberRepository;
import com.hcl.cms.security.JwtUtil;
import com.hcl.cms.service.AdminService;

import jakarta.servlet.http.HttpSession;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

	private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Override
	public String register(AdminRequest request) {
		logger.info("Attempting to register admin with ID: {}", request.getAdminId());

		if (adminRepository.findByAdminId(request.getAdminId()).isPresent()) {
			logger.warn("Admin registration failed. Admin already exists: {}", request.getAdminId());
			throw new ResourceAlreadyExistsException("Admin already registered");
		}

		Admin admin = new Admin();
		admin.setAdminId(request.getAdminId());
		admin.setPassword(encoder.encode(request.getPassword()));

		adminRepository.save(admin);
		logger.info("Admin registered successfully: {}", request.getAdminId());
		return "Admin registered successfully";
	}

	@Override
	public JwtResponse login(AdminRequest request, HttpSession session) {
		logger.info("Login attempt for admin ID: {}", request.getAdminId());

		Admin admin = adminRepository.findByAdminId(request.getAdminId())
				.orElseThrow(() -> {
					logger.error("Admin not found: {}", request.getAdminId());
					return new ResourceNotFoundException("Admin not found");
				});

		if (!encoder.matches(request.getPassword(), admin.getPassword())) {
			logger.error("Invalid credentials for admin: {}", request.getAdminId());
			throw new RuntimeException("Invalid credentials");
		}

		String token = jwtUtil.generateToken(admin, "ROLE_ADMIN");
		session.setAttribute("adminId", admin.getAdminId());

		logger.info("Login successful for admin: {}", request.getAdminId());
		return new JwtResponse(token);
	}

	@Override
	public String approveOrRejectMember(MemberApprovalRequest request) {
		logger.info("Processing approval for member ID: {}", request.getMemberId());

		Member member = memberRepository.findByMemberId(request.getMemberId())
				.orElseThrow(() -> {
					logger.error("Member not found for approval: {}", request.getMemberId());
					return new ResourceNotFoundException("Member not found");
				});

		if (!member.getStatus().equals(MemberStatus.PENDING)) {
			logger.warn("Approval failed. Member already processed: {} with status {}", request.getMemberId(), member.getStatus());
			throw new MemberAlreadyApprovedOrRejectException("Member already " + member.getStatus());
		}

		if (request.isApproval()) {
			member.setStatus(MemberStatus.APPROVED);
			member.setApproved(true);
			logger.info("Member approved: {}", request.getMemberId());
		} else {
			member.setStatus(MemberStatus.REJECTED);
			member.setApproved(false);
			logger.info("Member rejected: {}", request.getMemberId());
		}

		memberRepository.save(member);
		return "Member " + (request.isApproval() ? "approved" : "rejected") + " successfully.";
	}

	@Override
	public List<Member> getPendingMembers() {
		logger.info("Fetching pending members.");
		return memberRepository.findByStatus(MemberStatus.PENDING);
	}

	@Override
	public List<Member> getAllMembers() {
		logger.info("Fetching all members.");
		return memberRepository.findAll();
	}

	@Override
	public MemberDetailsResponse getMemberByMemberId(String memberId) {
		logger.info("Fetching member by ID: {}", memberId);
		Member m = memberRepository.findByMemberId(memberId)
				.orElseThrow(() -> {
					logger.error("Member not found with ID: {}", memberId);
					return new ResourceNotFoundException("Member not found with ID: " + memberId);
				});
		return toDto(m);
	}

	private MemberDetailsResponse toDto(Member member) {
		logger.debug("Converting member entity to DTO for ID: {}", member.getMemberId());
		return new MemberDetailsResponse(
				member.getEmail(),
				member.getMemberId(),
				member.getFirstName(),
				member.getLastName(),
				member.getAge(),
				member.getGender(),
				member.getContactNumber(),
				member.getStatus()
		);
	}
}
