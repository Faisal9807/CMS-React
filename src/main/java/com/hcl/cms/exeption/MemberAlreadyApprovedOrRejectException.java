package com.hcl.cms.exeption;

public class MemberAlreadyApprovedOrRejectException  extends RuntimeException {
	   public MemberAlreadyApprovedOrRejectException(String message) {
	        super(message);
	    }
}
