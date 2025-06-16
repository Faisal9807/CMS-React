package com.hcl.cms.dto;

public class ClaimResponse {
	private Long claimId;
	private String memberId;
	private String claimType;
	private String description;
	private String status;
	private Double amount;
	private String remarks;

	public ClaimResponse(Long claimId, String memberId, String claimType, String description, String status,
			Double amount, String remarks) {
		super();
		this.claimId = claimId;
		this.memberId = memberId;
		this.claimType = claimType;
		this.description = description;
		this.status = status;
		this.amount = amount;
		this.remarks = remarks;
	}

	public ClaimResponse() {
		super();
	}

	public Long getClaimId() {
		return claimId;
	}

	public void setClaimId(Long claimId) {
		this.claimId = claimId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
