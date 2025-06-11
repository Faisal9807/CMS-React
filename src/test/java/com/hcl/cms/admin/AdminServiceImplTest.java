package com.hcl.cms.admin;

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
import com.hcl.cms.service.impl.AdminServiceImpl;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {

    @InjectMocks
    private AdminServiceImpl adminService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Spy
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        AdminRequest request = new AdminRequest("admin1", "pass123");
        when(adminRepository.findByAdminId("admin1")).thenReturn(Optional.empty());

        String result = adminService.register(request);

        assertEquals("Admin registered successfully", result);
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void testRegister_AlreadyExists() {
        AdminRequest request = new AdminRequest("admin1", "pass123");
        when(adminRepository.findByAdminId("admin1")).thenReturn(Optional.of(new Admin()));

        assertThrows(ResourceAlreadyExistsException.class, () -> adminService.register(request));
    }

    @Test
    void testLogin_Success() {
        String rawPassword = "secret";
        String encodedPassword = encoder.encode(rawPassword);
        Admin admin = new Admin();
        admin.setAdminId("admin1");
        admin.setPassword(encodedPassword);

        when(adminRepository.findByAdminId("admin1")).thenReturn(Optional.of(admin));
        when(jwtUtil.generateToken("admin1", "ROLE_ADMIN")).thenReturn("mockJwt");

        JwtResponse response = adminService.login(new AdminRequest("admin1", rawPassword), session);

        assertEquals("mockJwt", response.getToken());
        verify(session).setAttribute("adminId", "admin1");
    }

    @Test
    void testLogin_InvalidCredentials() {
        Admin admin = new Admin();
        admin.setAdminId("admin1");
        admin.setPassword(encoder.encode("correctPass"));

        when(adminRepository.findByAdminId("admin1")).thenReturn(Optional.of(admin));

        assertThrows(RuntimeException.class, () -> adminService.login(new AdminRequest("admin1", "wrongPass"), session));
    }

    @Test
    void testLogin_AdminNotFound() {
        when(adminRepository.findByAdminId("admin1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.login(new AdminRequest("admin1", "pass"), session));
    }

    @Test
    void testApproveMember_Success() {
        Member member = new Member();
        member.setMemberId("mem1");
        member.setStatus(MemberStatus.PENDING);
        when(memberRepository.findByMemberId("mem1")).thenReturn(Optional.of(member));

        MemberApprovalRequest request = new MemberApprovalRequest("mem1", true);
        String result = adminService.approveOrRejectMember(request);

        assertEquals("Member approved successfully.", result);
        assertTrue(member.isApproved());
        assertEquals(MemberStatus.APPROVED, member.getStatus());
        verify(memberRepository).save(member);
    }

    @Test
    void testRejectMember_Success() {
        Member member = new Member();
        member.setMemberId("mem1");
        member.setStatus(MemberStatus.PENDING);
        when(memberRepository.findByMemberId("mem1")).thenReturn(Optional.of(member));

        MemberApprovalRequest request = new MemberApprovalRequest("mem1", false);
        String result = adminService.approveOrRejectMember(request);

        assertEquals("Member rejected successfully.", result);
        assertFalse(member.isApproved());
        assertEquals(MemberStatus.REJECTED, member.getStatus());
        verify(memberRepository).save(member);
    }

    @Test
    void testApproveOrRejectMember_AlreadyApproved() {
        Member member = new Member();
        member.setMemberId("mem1");
        member.setStatus(MemberStatus.APPROVED);
        when(memberRepository.findByMemberId("mem1")).thenReturn(Optional.of(member));

        MemberApprovalRequest request = new MemberApprovalRequest("mem1", false);

        MemberAlreadyApprovedOrRejectException exception =
                assertThrows(MemberAlreadyApprovedOrRejectException.class, () -> adminService.approveOrRejectMember(request));

        assertEquals("Member already APPROVED", exception.getMessage());
    }

    @Test
    void testApproveOrRejectMember_AlreadyRejected() {
        Member member = new Member();
        member.setMemberId("mem1");
        member.setStatus(MemberStatus.REJECTED);
        when(memberRepository.findByMemberId("mem1")).thenReturn(Optional.of(member));

        MemberApprovalRequest request = new MemberApprovalRequest("mem1", true);

        MemberAlreadyApprovedOrRejectException exception =
                assertThrows(MemberAlreadyApprovedOrRejectException.class, () -> adminService.approveOrRejectMember(request));

        assertEquals("Member already REJECTED", exception.getMessage());
    }

    @Test
    void testApproveOrRejectMember_MemberNotFound() {
        when(memberRepository.findByMemberId("mem1")).thenReturn(Optional.empty());
        MemberApprovalRequest request = new MemberApprovalRequest("mem1", true);

        assertThrows(ResourceNotFoundException.class, () -> adminService.approveOrRejectMember(request));
    }

    @Test
    void testGetPendingMembers() {
        List<Member> pendingList = List.of(new Member(), new Member());
        when(memberRepository.findByStatus(MemberStatus.PENDING)).thenReturn(pendingList);

        List<Member> result = adminService.getPendingMembers();

        assertEquals(2, result.size());
        verify(memberRepository).findByStatus(MemberStatus.PENDING);
    }

    @Test
    void testGetAllMembers() {
        List<Member> allMembers = List.of(new Member(), new Member(), new Member());
        when(memberRepository.findAll()).thenReturn(allMembers);

        List<Member> result = adminService.getAllMembers();

        assertEquals(3, result.size());
        verify(memberRepository).findAll();
    }

    @Test
    void testGetMemberByMemberId_Success() {
        Member member = new Member();
        member.setMemberId("mem1");
        member.setFirstName("John");
        member.setLastName("Doe");
        member.setAge(30);
        member.setGender("Male");
        member.setContactNumber("1234567890");
        member.setStatus(MemberStatus.PENDING);

        when(memberRepository.findByMemberId("mem1")).thenReturn(Optional.of(member));

        MemberDetailsResponse response = adminService.getMemberByMemberId("mem1");

        assertEquals("mem1", response.getMemberId());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals(30, response.getAge());
        assertEquals("Male", response.getGender());
        assertEquals("1234567890", response.getContactNumber());
        assertEquals(MemberStatus.PENDING, response.getStatus());
    }

    @Test
    void testGetMemberByMemberId_NotFound() {
        when(memberRepository.findByMemberId("mem1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.getMemberByMemberId("mem1"));
    }
}
