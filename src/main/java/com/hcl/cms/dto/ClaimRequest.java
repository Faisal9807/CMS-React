package com.hcl.cms.dto;

public class ClaimRequest {
	private String claimType;
	private String description;
	private Double amount;
	
	public ClaimRequest(String claimType, String description, Double amount) {
		super();
		this.claimType = claimType;
		this.description = description;
		this.amount = amount;
	}
	public ClaimRequest() {
		super();
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
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	
}
