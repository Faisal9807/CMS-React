package com.hcl.cms.service;

import java.util.List;

import com.hcl.cms.dto.ClaimProcessingRequest;
import com.hcl.cms.dto.ClaimRequest;
import com.hcl.cms.dto.ClaimResponse;
import com.hcl.cms.entity.Claim;
import com.hcl.cms.enums.ClaimStatus;

public interface ClaimService {
	Claim raiseClaim(String token,ClaimRequest request);
	List<ClaimResponse> getClaimsByMember(String memberId);
	String processClaim(ClaimProcessingRequest request);
	List<ClaimResponse> getAllClaims();
	List<ClaimResponse> getClaimsByStatus(ClaimStatus status);
	List<ClaimResponse> getClaimsForLoggedInMember(String token);

}
