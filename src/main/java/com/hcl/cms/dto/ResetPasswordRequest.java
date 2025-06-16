package com.hcl.cms.dto;

public class ResetPasswordRequest {
	private String newPassword;

	public ResetPasswordRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResetPasswordRequest(String newPassword) {
		super();
		this.newPassword = newPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	
}
