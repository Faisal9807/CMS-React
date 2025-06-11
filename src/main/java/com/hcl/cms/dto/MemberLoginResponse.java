package com.hcl.cms.dto;

public class MemberLoginResponse {
	private String token;

	public MemberLoginResponse(String token) {
		this.token = token;
	}

	// getter
	public String getToken() {
		return token;
	}
}
