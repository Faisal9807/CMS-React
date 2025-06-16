package com.hcl.cms.service;

import com.hcl.cms.dto.ContactUsRequest;
import com.hcl.cms.dto.MemberLoginRequest;
import com.hcl.cms.dto.MemberRequest;
import com.hcl.cms.dto.MemberUpdateRequest;
import jakarta.servlet.http.HttpSession;

import com.hcl.cms.entity.ContactUs;
import com.hcl.cms.entity.Member;

public interface MemberService {
    String registerMember(MemberRequest request);
    String login(MemberLoginRequest request,HttpSession session);
    Member updateMember(String token, MemberUpdateRequest dto);
	String contactUs(String token, ContactUsRequest request);
}
