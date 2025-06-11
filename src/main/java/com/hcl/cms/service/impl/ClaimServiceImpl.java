package com.hcl.cms.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.cms.dto.ClaimProcessingRequest;
import com.hcl.cms.dto.ClaimRequest;
import com.hcl.cms.dto.ClaimResponse;
import com.hcl.cms.entity.Claim;
import com.hcl.cms.entity.Member;
import com.hcl.cms.enums.ClaimStatus;
import com.hcl.cms.exeption.ResourceNotFoundException;
import com.hcl.cms.repository.ClaimRepository;
import com.hcl.cms.repository.MemberRepository;
import com.hcl.cms.service.ClaimService;
import com.hcl.cms.exeption.ClaimAlreadyProcessedException;
import com.hcl.cms.security.JwtUtil;

@Service
public class ClaimServiceImpl implements ClaimService {

	private static final Logger logger = LoggerFactory.getLogger(ClaimServiceImpl.class);

	@Autowired
	private ClaimRepository claimRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public Claim raiseClaim(String token, ClaimRequest request) {
		String memberId = jwtUtil.extractUsername(token);
		logger.info("Raising new claim for member ID: {}", memberId);

		Member member = memberRepository.findByMemberId(memberId)
				.orElseThrow(() -> {
					logger.error("Member not found for ID: {}", memberId);
					return new ResourceNotFoundException("Member not found");
				});

		Claim claim = new Claim();
		claim.setMember(member);
		claim.setClaimType(request.getClaimType());
		claim.setDescription(request.getDescription());
		claim.setAmount(request.getAmount());
		claim.setStatus(ClaimStatus.PENDING);
		claim.setRemarks("Awaiting review");

		Claim saved = claimRepository.save(claim);
		logger.info("Claim submitted successfully. Claim ID: {}", saved.getId());
		return saved;
	}

	@Override
	public List<ClaimResponse> getClaimsByMember(String memberId) {
		logger.info("Fetching claims for member ID: {}", memberId);
		List<Claim> claims = claimRepository.findByMember_MemberId(memberId);

		if (claims.isEmpty()) {
			logger.warn("No claims found for member ID: {}", memberId);
			throw new ResourceNotFoundException("No claims found for member ID: " + memberId);
		}

		return claims.stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	@Override
	public List<ClaimResponse> getClaimsForLoggedInMember(String token) {
		String actualToken = token.substring(7); // Remove "Bearer " prefix
		String memberId = jwtUtil.extractUsername(actualToken);
		logger.info("Fetching claims for logged-in member ID: {}", memberId);

		Member member = memberRepository.findByMemberId(memberId)
				.orElseThrow(() -> {
					logger.error("Member not found: {}", memberId);
					return new ResourceNotFoundException("Member not found");
				});

		List<Claim> claims = claimRepository.findByMember_MemberId(memberId);
		if (claims.isEmpty()) {
			logger.warn("No claims found for member ID: {}", memberId);
			throw new ResourceNotFoundException("No claims found for member ID: " + memberId);
		}

		return claims.stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	@Override
	public String processClaim(ClaimProcessingRequest request) {
		logger.info("Processing claim with ID: {}", request.getClaimId());

		Claim claim = claimRepository.findById(request.getClaimId())
				.orElseThrow(() -> {
					logger.error("Claim not found: {}", request.getClaimId());
					return new ResourceNotFoundException("Claim not found");
				});

		if (!claim.getStatus().equals(ClaimStatus.PENDING)) {
			logger.warn("Claim already processed. Status: {}", claim.getStatus());
			throw new ClaimAlreadyProcessedException("Claim already " + claim.getStatus());
		}

		if (request.isApproved()) {
			claim.setStatus(ClaimStatus.APPROVED);
			claim.setAmount(request.getApprovedAmount());
			claim.setRemarks(request.getRemarks());
			logger.info("Claim approved with amount: {}", request.getApprovedAmount());
		} else {
			claim.setStatus(ClaimStatus.REJECTED);
			claim.setRemarks(request.getRemarks());
			logger.info("Claim rejected. Remarks: {}", request.getRemarks());
		}

		claimRepository.save(claim);
		return "Claim " + (request.isApproved() ? "approved" : "rejected") + " successfully.";
	}

	@Override
	public List<ClaimResponse> getAllClaims() {
		logger.info("Fetching all claims");
		List<Claim> claims = claimRepository.findAll();
		return claims.stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	@Override
	public List<ClaimResponse> getClaimsByStatus(ClaimStatus status) {
		logger.info("Fetching claims with status: {}", status);
		List<Claim> claims = claimRepository.findByStatus(status);
		return claims.stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	private ClaimResponse mapToResponse(Claim claim) {
		ClaimResponse response = new ClaimResponse();
		response.setClaimId(claim.getId());
		response.setMemberId(claim.getMember().getMemberId());
		response.setClaimType(claim.getClaimType());
		response.setDescription(claim.getDescription());
		response.setStatus(claim.getStatus().name());
		response.setAmount(claim.getAmount());
		response.setRemarks(claim.getRemarks());
		return response;
	}
}
