package com.hcl.cms.dto;
import jakarta.validation.constraints.*;

public class MemberUpdateRequest {

 
    @NotBlank(message = "First name is required")
    private String firstName;
 
    @NotBlank(message = "Last name is required")
    private String lastName;
 
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must not exceed 100")
    private int age;
 
    @NotBlank(message = "Gender is required")
    private String gender;
 
    @NotBlank(message = "Contact number is required")
    @Pattern(regexp ="\\d{10}", message = "Contact Number must be of 10 didgits")
    private String contactNumber;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
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

	public MemberUpdateRequest(@NotBlank(message = "First name is required") String firstName,
			@NotBlank(message = "Last name is required") String lastName,
			@Min(value = 18, message = "Age must be at least 18") @Max(value = 100, message = "Age must not exceed 100") int age,
			@NotBlank(message = "Gender is required") String gender,
			@NotBlank(message = "Contact number is required") String contactNumber) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.gender = gender;
		this.contactNumber = contactNumber;
	}

	public MemberUpdateRequest() {

	}
}