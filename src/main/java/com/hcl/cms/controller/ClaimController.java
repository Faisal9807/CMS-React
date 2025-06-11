package com.hcl.cms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hcl.cms.dto.ClaimRequest;
import com.hcl.cms.dto.ClaimResponse;
import com.hcl.cms.entity.Claim;
import com.hcl.cms.service.ClaimService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/member/claims")
@SecurityRequirement(name = "bearerAuth")
//@CrossOrigin(origins = "http://localhost:3000")
public class ClaimController {

    private static final Logger logger = LoggerFactory.getLogger(ClaimController.class);

    @Autowired
    private ClaimService claimService;

    @PostMapping("/submit")
    public ResponseEntity<Claim> submitClaim(@RequestHeader("Authorization") String token,
                                             @Valid @RequestBody ClaimRequest request) {
        logger.info("Received request to submit claim for token: {}", maskToken(token));
        Claim claim = claimService.raiseClaim(token, request);
        logger.info("Claim submitted successfully with ID: {}", claim.getId());
        return ResponseEntity.ok(claim);
    }

    @PostMapping("/all")
    public ResponseEntity<List<ClaimResponse>> getClaimsForLoggedInMember(
            @RequestHeader("Authorization") String token) {
        logger.info("Fetching claims for logged-in member. Token received: {}", maskToken(token));
        List<ClaimResponse> response = claimService.getClaimsForLoggedInMember(token);
        logger.info("Total claims retrieved: {}", response.size());
        return ResponseEntity.ok(response);
    }

    // Optional helper method to avoid logging full tokens
    private String maskToken(String token) {
        if (token == null || token.length() < 10) return "InvalidToken";
        return token.substring(0, 10) + "...";
    }
}
