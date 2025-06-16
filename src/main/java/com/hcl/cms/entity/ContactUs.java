package com.hcl.cms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
public class ContactUs {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private String memberId;
	@NotBlank
	private String name;
	@NotBlank
	private String phone;
	@NotBlank
	private String emal;
	@NotBlank
	private String message;
	public ContactUs() {
		
	}
	
	public ContactUs(Long id, String memberId, String name, String phone, String emal, String message) {
		super();
		this.id = id;
		this.memberId = memberId;
		this.name = name;
		this.phone = phone;
		this.emal = emal;
		this.message = message;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
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
	public String getEmal() {
		return emal;
	}
	public void setEmal(String emal) {
		this.emal = emal;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
