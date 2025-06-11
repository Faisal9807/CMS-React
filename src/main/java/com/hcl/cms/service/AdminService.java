package com.hcl.cms.service;

import com.hcl.cms.dto.AdminRequest;
import com.hcl.cms.dto.JwtResponse;
import com.hcl.cms.dto.MemberApprovalRequest;
import com.hcl.cms.dto.MemberDetailsResponse;
import com.hcl.cms.entity.Member;
import org.springframework.http.HttpStatus;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface AdminService {
	String register(AdminRequest reuest);
	JwtResponse login(AdminRequest request,HttpSession session);
	String approveOrRejectMember(MemberApprovalRequest request);
	List<Member> getPendingMembers();
	List<Member> getAllMembers();
	MemberDetailsResponse getMemberByMemberId(String memberId);
}
