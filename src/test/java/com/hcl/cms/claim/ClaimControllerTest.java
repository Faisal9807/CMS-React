package com.hcl.cms.claim;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.cms.controller.ClaimController;
import com.hcl.cms.dto.ClaimRequest;
import com.hcl.cms.dto.ClaimResponse;
import com.hcl.cms.entity.Claim;
import com.hcl.cms.security.JwtUtil;
import com.hcl.cms.service.ClaimService;
import com.hcl.cms.service.CustomUserDetailsService;
import com.hcl.cms.config.JwtAuthenticationFilter;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClaimController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClaimControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClaimService claimService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    public void testSubmitClaim() throws Exception {
        ClaimRequest request = new ClaimRequest();
        request.setClaimType("Health");
        request.setDescription("Surgery expenses");
        request.setAmount(10000.0);

        Claim claim = new Claim();
        claim.setId(1L);

        String token = "Bearer mock-token";

        Mockito.when(claimService.raiseClaim(eq(token), any(ClaimRequest.class))).thenReturn(claim);

        mockMvc.perform(post("/api/member/claims/submit")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testGetClaimsForLoggedInMember() throws Exception {
        ClaimResponse response = new ClaimResponse();
        response.setClaimId(1L);
        response.setClaimType("Health");
        response.setDescription("Surgery");
        response.setStatus("PENDING");
        response.setAmount(10000.0);
        response.setRemarks("Waiting for approval");

        List<ClaimResponse> responses = Arrays.asList(response);

        String token = "Bearer mock-token";

        Mockito.when(claimService.getClaimsForLoggedInMember(token)).thenReturn(responses);

        mockMvc.perform(post("/api/member/claims/all")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].claimId").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }
}
