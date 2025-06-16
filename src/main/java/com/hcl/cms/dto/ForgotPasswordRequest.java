package com.hcl.cms.dto;

public class ForgotPasswordRequest {
	private String email;

	public ForgotPasswordRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ForgotPasswordRequest(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
