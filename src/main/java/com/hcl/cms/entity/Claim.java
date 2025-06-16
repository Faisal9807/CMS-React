package com.hcl.cms.entity;

import com.hcl.cms.enums.ClaimStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;


@Entity
public class Claim {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne
    private Member member;
    @NotBlank(message = "Claim type is required")
    private String claimType;
    @NotBlank(message = "Description is required")
    private String description;
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private double amount;
 
    @Enumerated(EnumType.STRING)
    private ClaimStatus status;
 
    private String remarks;

	public Claim(Long id, Member member, String claimType, String description, double amount, ClaimStatus status,
			String remarks) {
		super();
		this.id = id;
		this.member = member;
		this.claimType = claimType;
		this.description = description;
		this.amount = amount;
		this.status = status;
		this.remarks = remarks;
	}

	public Claim() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public ClaimStatus getStatus() {
		return status;
	}

	public void setStatus(ClaimStatus status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
    
}
