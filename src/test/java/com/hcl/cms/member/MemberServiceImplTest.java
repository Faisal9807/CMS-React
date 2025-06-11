package com.hcl.cms.member;

import com.hcl.cms.dto.MemberLoginRequest;
import com.hcl.cms.dto.MemberRequest;
import com.hcl.cms.dto.MemberUpdateRequest;
import com.hcl.cms.entity.Member;
import com.hcl.cms.enums.MemberStatus;
import com.hcl.cms.exeption.ResourceAlreadyExistsException;
import com.hcl.cms.exeption.ResourceNotFoundException;
import com.hcl.cms.repository.MemberRepository;
import com.hcl.cms.security.JwtUtil;
import com.hcl.cms.service.impl.MemberServiceImpl;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private HttpSession session;

    private MemberRequest memberRequest;
    private MemberLoginRequest loginRequest;
    private Member member;
    private MemberUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        memberRequest = new MemberRequest();
        memberRequest.setFirstName("John");
        memberRequest.setLastName("Doe");
        memberRequest.setAge(25);
        memberRequest.setGender("Male");
        memberRequest.setContactNumber("1234567890");
        memberRequest.setMemberId("mem001");
        memberRequest.setPassword("password123");

        loginRequest = new MemberLoginRequest();
        loginRequest.setMemberId("mem001");
        loginRequest.setPassword("password123");

        member = new Member();
        member.setMemberId("mem001");
        member.setPassword("encodedPassword");
        member.setStatus(MemberStatus.APPROVED);
        member.setApproved(true);

        updateRequest = new MemberUpdateRequest();
        updateRequest.setFirstName("Jane");
        updateRequest.setLastName("Smith");
        updateRequest.setGender("Female");
        updateRequest.setAge(30);
        updateRequest.setContactNumber("9998887777");
    }

    @Test
    void testRegisterMember_Success() {
        when(memberRepository.findByMemberId("mem001")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        String result = memberService.registerMember(memberRequest);

        assertEquals("Your details are submitted successfully", result);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void testRegisterMember_AlreadyExists() {
        when(memberRepository.findByMemberId("mem001")).thenReturn(Optional.of(new Member()));

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            memberService.registerMember(memberRequest);
        });

        verify(memberRepository, never()).save(any());
    }

    @Test
    void testLogin_Success() {
        when(memberRepository.findByMemberId("mem001")).thenReturn(Optional.of(member));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("mem001", "ROLE_MEMBER")).thenReturn("mock-jwt-token");

        String token = memberService.login(loginRequest, session);

        assertEquals("mock-jwt-token", token);
        verify(session).setAttribute("memberId", "mem001");
    }

    @Test
    void testLogin_MemberNotFound() {
        when(memberRepository.findByMemberId("mem001")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            memberService.login(loginRequest, session);
        });
    }

    @Test
    void testLogin_InvalidPassword() {
        when(memberRepository.findByMemberId("mem001")).thenReturn(Optional.of(member));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            memberService.login(loginRequest, session);
        });
    }

    @Test
    void testLogin_NotApproved() {
        member.setStatus(MemberStatus.PENDING);
        when(memberRepository.findByMemberId("mem001")).thenReturn(Optional.of(member));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> {
            memberService.login(loginRequest, session);
        });
    }

    @Test
    void testUpdateMember_Success() {
        when(jwtUtil.extractUsername("Bearer token")).thenReturn("mem001");
        when(memberRepository.findByMemberId("mem001")).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        Member updated = memberService.updateMember("Bearer token", updateRequest);

        assertEquals("Jane", updated.getFirstName());
        assertEquals("Smith", updated.getLastName());
        assertEquals("Female", updated.getGender());
        assertEquals(30, updated.getAge());
        assertEquals("9998887777", updated.getContactNumber());
    }

    @Test
    void testUpdateMember_NotFound() {
        when(jwtUtil.extractUsername("Bearer token")).thenReturn("mem001");
        when(memberRepository.findByMemberId("mem001")).thenReturn(Optional.empty());
    }
}