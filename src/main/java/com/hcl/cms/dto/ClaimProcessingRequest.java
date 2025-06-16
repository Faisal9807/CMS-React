package com.hcl.cms.dto;

public class ClaimProcessingRequest {
	private Long claimId;
	private boolean approved;
	private String remarks;
	private Double approvedAmount;
	public ClaimProcessingRequest(Long claimId, boolean approved, String remarks, Double approvedAmount) {
		super();
		this.claimId = claimId;
		this.approved = approved;
		this.remarks = remarks;
		this.approvedAmount = approvedAmount;
	}
	public ClaimProcessingRequest() {
		super();
	}
	public Long getClaimId() {
		return claimId;
	}
	public void setClaimId(Long claimId) {
		this.claimId = claimId;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Double getApprovedAmount() {
		return approvedAmount;
	}
	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}
	
	
}
