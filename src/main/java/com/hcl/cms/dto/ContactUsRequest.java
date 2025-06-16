package com.hcl.cms.dto;

public class ContactUsRequest {
	private String name;
	private String phone;
	private String email;
	private String message;
	
	
	public ContactUsRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ContactUsRequest(String name, String phone, String email, String message) {
		super();
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.message = message;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
