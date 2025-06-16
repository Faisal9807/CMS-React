package com.hcl.cms.dto;

public class MemberApprovalRequest {
	private String memberId;
	private boolean approval;
	public MemberApprovalRequest(String memberId, boolean approval) {
		super();
		this.memberId = memberId;
		this.approval = approval;
	}
	public MemberApprovalRequest() {
		super();
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public boolean isApproval() {
		return approval;
	}
	public void setApproval(boolean approval) {
		this.approval = approval;
	}
	
}
