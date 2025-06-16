package com.hcl.cms.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.cms.controller.MemberController;
import com.hcl.cms.dto.MemberLoginRequest;
import com.hcl.cms.dto.MemberLoginResponse;
import com.hcl.cms.dto.MemberRequest;
import com.hcl.cms.dto.MemberUpdateRequest;
import com.hcl.cms.entity.Member;
import com.hcl.cms.security.JwtUtil;
import com.hcl.cms.service.CustomUserDetailsService;
import com.hcl.cms.service.MemberService;
import com.hcl.cms.config.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private MemberRequest memberRequest;
    private MemberLoginRequest loginRequest;
    private MemberUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        memberRequest = new MemberRequest();
        memberRequest.setMemberId("mem001");
        memberRequest.setPassword("secret");
        memberRequest.setFirstName("John");
        memberRequest.setLastName("Doe");
        memberRequest.setGender("Male");
        memberRequest.setAge(25);
        memberRequest.setContactNumber("9807622880");

        loginRequest = new MemberLoginRequest();
        loginRequest.setMemberId("mem001");
        loginRequest.setPassword("secret");

        updateRequest = new MemberUpdateRequest();
        updateRequest.setFirstName("Jane");
        updateRequest.setLastName("Smith");
        updateRequest.setGender("Female");
        updateRequest.setAge(28);
        updateRequest.setContactNumber("9876543210");
    }

    @Test
    void testRegisterMember() throws Exception {
        when(memberService.registerMember(any(MemberRequest.class))).thenReturn("Member registered successfully");

        mockMvc.perform(post("/api/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Member registered successfully"));
    }

    @Test
    void testLoginMember() throws Exception {
        when(memberService.login(any(MemberLoginRequest.class), any(HttpSession.class))).thenReturn("mock-jwt-token");

        mockMvc.perform(post("/api/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(new MockHttpSession())
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }

    @Test
    void testLogoutMember() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("memberId", "mem001");

        mockMvc.perform(post("/api/members/logout").session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout Successful"));
    }

    @Test
    void testUpdateMemberDetails() throws Exception {
        Member updatedMember = new Member();
        updatedMember.setMemberId("mem001");
        updatedMember.setFirstName("Jane");
        updatedMember.setLastName("Smith");
        updatedMember.setGender("Female");
        updatedMember.setAge(28);
        updatedMember.setContactNumber("9876543210");

        when(memberService.updateMember(eq("Bearer mock-token"), any(MemberUpdateRequest.class))).thenReturn(updatedMember);

        mockMvc.perform(put("/api/members/update")
                        .header("Authorization", "Bearer mock-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.age").value(28))
                .andExpect(jsonPath("$.gender").value("Female"))
                .andExpect(jsonPath("$.contactNumber").value("9876543210"));
    }
}
