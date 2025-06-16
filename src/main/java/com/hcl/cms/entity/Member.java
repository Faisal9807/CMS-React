package com.hcl.cms.entity;

import com.hcl.cms.enums.MemberStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity

public class Member {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @NotBlank(message = "First name is mandatory")
    private String firstName;
 
    @NotBlank(message = "Last name is mandatory")
    private String lastName;
 
    @Min(18)
    @Max(100)
    private int age;
	@NotBlank
	private String email;
 
    @NotBlank
    private String gender;
 
    @NotBlank
    private String contactNumber;
 
    @Column(unique = true)
    @NotBlank(message = "Member ID is mandatory")
    private String memberId;
 
    @NotBlank(message = "Password is required")
    private String password;
    
    @Enumerated(EnumType.STRING)
    private MemberStatus status=MemberStatus.PENDING;

	public Member(String email,Long id, String firstName, boolean approved, MemberStatus status, String password, String memberId, String contactNumber, String gender, int age, String lastName) {
		this.email=email;
		this.id = id;
		this.firstName = firstName;
		this.approved = approved;
		this.status = status;
		this.password = password;
		this.memberId = memberId;
		this.contactNumber = contactNumber;
		this.gender = gender;
		this.age = age;
		this.lastName = lastName;
	}

	public Member() {
	}

	public MemberStatus getStatus() {
		return status;
	}

	public void setStatus(MemberStatus status) {
		this.status = status;
	}

	private boolean approved = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
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

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}
    
}