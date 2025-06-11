package com.hcl.cms.claim;

import com.hcl.cms.dto.ClaimProcessingRequest;
import com.hcl.cms.dto.ClaimRequest;
import com.hcl.cms.dto.ClaimResponse;
import com.hcl.cms.entity.Claim;
import com.hcl.cms.entity.Member;
import com.hcl.cms.enums.ClaimStatus;
import com.hcl.cms.exeption.ClaimAlreadyProcessedException;
import com.hcl.cms.exeption.ResourceNotFoundException;
import com.hcl.cms.repository.ClaimRepository;
import com.hcl.cms.repository.MemberRepository;
import com.hcl.cms.security.JwtUtil;

import com.hcl.cms.service.impl.ClaimServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClaimServiceImplTest {

    @InjectMocks
    private ClaimServiceImpl claimService;

    @Mock
    private ClaimRepository claimRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtUtil jwtUtil;

    private Member member;
    private Claim claim;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        member = new Member();
        member.setMemberId("M123");

        claim = new Claim();
        claim.setId(1L);
        claim.setMember(member);
        claim.setClaimType("Health");
        claim.setDescription("Medical expense");
        claim.setAmount(1000.0);
        claim.setStatus(ClaimStatus.PENDING);
        claim.setRemarks("Awaiting review");
    }

    @Test
    void testRaiseClaim_success() {
        ClaimRequest request = new ClaimRequest();
        request.setClaimType("Health");
        request.setDescription("Medical expense");
        request.setAmount(1000.0);

        when(jwtUtil.extractUsername("token")).thenReturn("M123");
        when(memberRepository.findByMemberId("M123")).thenReturn(Optional.of(member));
        when(claimRepository.save(any(Claim.class))).thenReturn(claim);

        Claim result = claimService.raiseClaim("token", request);

        assertEquals("Health", result.getClaimType());
        assertEquals(1000.0, result.getAmount());
        assertEquals(ClaimStatus.PENDING, result.getStatus());
    }

    @Test
    void testRaiseClaim_memberNotFound() {
        when(jwtUtil.extractUsername("token")).thenReturn("M123");
        when(memberRepository.findByMemberId("M123")).thenReturn(Optional.empty());

        ClaimRequest request = new ClaimRequest();
        request.setClaimType("Health");

        assertThrows(ResourceNotFoundException.class, () -> claimService.raiseClaim("token", request));
    }

    @Test
    void testGetClaimsByMember_success() {
        when(claimRepository.findByMember_MemberId("M123")).thenReturn(List.of(claim));

        List<ClaimResponse> responses = claimService.getClaimsByMember("M123");

        assertEquals(1, responses.size());
        assertEquals("Health", responses.get(0).getClaimType());
    }

    @Test
    void testGetClaimsByMember_noClaimsFound() {
        when(claimRepository.findByMember_MemberId("M123")).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> claimService.getClaimsByMember("M123"));
    }

    @Test
    void testGetClaimsForLoggedInMember_success() {
        when(jwtUtil.extractUsername("token")).thenReturn("M123");
        when(memberRepository.findByMemberId("M123")).thenReturn(Optional.of(member));
        when(claimRepository.findByMember_MemberId("M123")).thenReturn(List.of(claim));

        List<ClaimResponse> responses = claimService.getClaimsForLoggedInMember("Bearer token");

        assertEquals(1, responses.size());
    }

    @Test
    void testProcessClaim_approved() {
        ClaimProcessingRequest request = new ClaimProcessingRequest();
        request.setClaimId(1L);
        request.setApproved(true);
        request.setApprovedAmount(900.0);
        request.setRemarks("Approved by admin");

        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));

        String response = claimService.processClaim(request);

        assertEquals("Claim approved successfully.", response);
        assertEquals(ClaimStatus.APPROVED, claim.getStatus());
    }

    @Test
    void testProcessClaim_rejected() {
        ClaimProcessingRequest request = new ClaimProcessingRequest();
        request.setClaimId(1L);
        request.setApproved(false);
        request.setRemarks("Insufficient documents");

        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));

        String response = claimService.processClaim(request);

        assertEquals("Claim rejected successfully.", response);
        assertEquals(ClaimStatus.REJECTED, claim.getStatus());
    }

    @Test
    void testProcessClaim_alreadyProcessed() {
        claim.setStatus(ClaimStatus.APPROVED);

        ClaimProcessingRequest request = new ClaimProcessingRequest();
        request.setClaimId(1L);
        request.setApproved(true);

        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));

        assertThrows(ClaimAlreadyProcessedException.class, () -> claimService.processClaim(request));
    }

    @Test
    void testGetAllClaims() {
        when(claimRepository.findAll()).thenReturn(List.of(claim));

        List<ClaimResponse> responses = claimService.getAllClaims();

        assertEquals(1, responses.size());
    }

    @Test
    void testGetClaimsByStatus() {
        when(claimRepository.findByStatus(ClaimStatus.PENDING)).thenReturn(List.of(claim));

        List<ClaimResponse> responses = claimService.getClaimsByStatus(ClaimStatus.PENDING);

        assertEquals(1, responses.size());
    }
}
