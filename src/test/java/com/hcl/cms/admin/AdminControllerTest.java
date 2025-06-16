package com.hcl.cms.admin;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.cms.config.JwtAuthenticationFilter;
import com.hcl.cms.controller.AdminController;
import com.hcl.cms.dto.*;
import com.hcl.cms.entity.Member;
import com.hcl.cms.enums.ClaimStatus;
import com.hcl.cms.security.JwtUtil;
import com.hcl.cms.service.AdminService;
import com.hcl.cms.service.ClaimService;
import com.hcl.cms.service.CustomUserDetailsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Collections;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

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

    private AdminRequest adminRequest;
    private MemberApprovalRequest approvalRequest;
    private ClaimProcessingRequest claimRequest;

    @BeforeEach
    void setUp() {
        adminRequest = new AdminRequest("admin1", "password");
        approvalRequest = new MemberApprovalRequest("member1", true);
        claimRequest = new ClaimProcessingRequest(1L, true, "Verified", 2000.0);
    }

    @Test
    void testRegister() throws Exception {
        when(adminService.register(any())).thenReturn("Admin registered successfully");

        mockMvc.perform(post("/api/admin/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Admin registered successfully"));
    }

    @Test
    void testLogin() throws Exception {
        when(adminService.login(any(), any())).thenReturn(new JwtResponse("token123"));

        mockMvc.perform(post("/api/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token123"));
    }

    @Test
    void testApproveMember() throws Exception {
        when(adminService.approveOrRejectMember(any())).thenReturn("Member approved successfully.");

        mockMvc.perform(patch("/api/admin/member/approval")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(approvalRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Member approved successfully."));
    }

    @Test
    void testProcessClaim() throws Exception {
        when(claimService.processClaim(any())).thenReturn("Claim processed successfully");

        mockMvc.perform(post("/api/admin/claims/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(claimRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Claim processed successfully"));
    }

    @Test
    void testGetAllClaims() throws Exception {
        ClaimResponse response = new ClaimResponse();
        when(claimService.getAllClaims()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/admin/all-claims"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetClaimsByStatus() throws Exception {
        ClaimResponse response = new ClaimResponse();
        when(claimService.getClaimsByStatus(ClaimStatus.PENDING)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/admin/claims-by-status")
                        .param("status", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetPendingMembers() throws Exception {
        Member member = new Member();
        when(adminService.getPendingMembers()).thenReturn(List.of(member));

        mockMvc.perform(get("/api/admin/members/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetAllMembers() throws Exception {
        Member member = new Member();
        when(adminService.getAllMembers()).thenReturn(List.of(member));

        mockMvc.perform(get("/api/admin/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post("/api/admin/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout Successful"));
    }

    @Test
    void testGetMemberById() throws Exception {
        MemberDetailsResponse memberDetails = new MemberDetailsResponse();
        when(adminService.getMemberByMemberId("member1")).thenReturn(memberDetails);

        mockMvc.perform(get("/api/admin/members/member1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetClaimsByMember() throws Exception {
        ClaimResponse response = new ClaimResponse();
        when(claimService.getClaimsByMember("member1")).thenReturn(List.of(response));

        mockMvc.perform(get("/api/admin/claims/member1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
