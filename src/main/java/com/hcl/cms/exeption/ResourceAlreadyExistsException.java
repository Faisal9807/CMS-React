package com.hcl.cms.exeption;

public class ResourceAlreadyExistsException extends RuntimeException {
	public ResourceAlreadyExistsException(String msg) {
		super(msg);
	}
}
