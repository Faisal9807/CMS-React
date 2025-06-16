package com.hcl.cms.controller;

import com.hcl.cms.dto.*;
import com.hcl.cms.entity.Member;
import com.hcl.cms.enums.ClaimStatus;
import com.hcl.cms.service.AdminService;
import com.hcl.cms.service.ClaimService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@SecurityRequirement(name = "bearerAuth")
//@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private ClaimService claimService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody AdminRequest request) {
        logger.info("Admin registration request received for username: {}", request.getAdminId());
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody AdminRequest request, HttpSession session) {
        logger.info("Admin login attempt for username: {}", request.getAdminId());
        return ResponseEntity.ok(adminService.login(request, session));
    }

    @PatchMapping("/member/approval")
    public ResponseEntity<String> approveMember(@RequestBody MemberApprovalRequest request) {
        logger.info("Processing member approval for memberId: {}", request.getMemberId());
        String response = adminService.approveOrRejectMember(request);
        logger.info("Approval response: {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/claims/process")
    public ResponseEntity<String> processClaim(@RequestBody ClaimProcessingRequest request) {
        logger.info("Processing claim ID: {}", request.getClaimId());
        String result = claimService.processClaim(request);
        logger.info("Claim process result: {}", result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all-claims")
    public ResponseEntity<List<ClaimResponse>> getAllClaims() {
        logger.info("Fetching all claims");
        return ResponseEntity.ok(claimService.getAllClaims());
    }

    @GetMapping("/claims-by-status")
    public ResponseEntity<List<ClaimResponse>> getClaimsByStatus(@RequestParam("status") ClaimStatus status) {
        logger.info("Fetching claims with status: {}", status);
        return ResponseEntity.ok(claimService.getClaimsByStatus(status));
    }

    @GetMapping("/members/pending")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Member>> getPendingMembers() {
        logger.info("Fetching pending members");
        List<Member> pendingMembers = adminService.getPendingMembers();
        logger.info("Pending members count: {}", pendingMembers.size());
        return ResponseEntity.ok(pendingMembers);
    }

    @GetMapping("/members")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Member>> getAllMembers() {
        logger.info("Fetching all members");
        List<Member> getAllMembers = adminService.getAllMembers();
        logger.info("Total members count: {}", getAllMembers.size());
        return ResponseEntity.ok(getAllMembers);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        logger.info("Admin logout requested");
        session.invalidate();
        return ResponseEntity.ok("Logout Successful");
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<MemberDetailsResponse> getMemberById(@PathVariable String memberId) {
        logger.info("Fetching member details for ID: {}", memberId);
        MemberDetailsResponse dto = adminService.getMemberByMemberId(memberId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/claims/{memberId}")
    public ResponseEntity<List<ClaimResponse>> getClaimsByMember(@PathVariable String memberId) {
        logger.info("Fetching claims for member ID: {}", memberId);
        List<ClaimResponse> response = claimService.getClaimsByMember(memberId);
        return ResponseEntity.ok(response);
    }
}
