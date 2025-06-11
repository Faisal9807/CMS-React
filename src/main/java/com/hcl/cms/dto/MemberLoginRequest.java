package com.hcl.cms.dto;

import jakarta.validation.constraints.NotBlank;

public class MemberLoginRequest {
	@NotBlank(message = "MemberId is required")
	private String memberId;
	@NotBlank(message = "Password is required")
	private String password;

	public MemberLoginRequest(String memberId, String password) {
		super();
		this.memberId = memberId;
		this.password = password;
	}

	public MemberLoginRequest() {
		super();
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
