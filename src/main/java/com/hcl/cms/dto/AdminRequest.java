package com.hcl.cms.dto;

import jakarta.validation.constraints.NotBlank;

public class AdminRequest {
	@NotBlank(message = "AdminId is required")
	private String adminId;
	
	@NotBlank(message="Password is required")
	private String password;

	public AdminRequest(@NotBlank String adminId, @NotBlank String password) {
		super();
		this.adminId = adminId;
		this.password = password;
	}

	public AdminRequest() {
		super();
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
