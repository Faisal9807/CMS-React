package com.hcl.cms.exeption;


public class ClaimAlreadyProcessedException extends RuntimeException {
    public ClaimAlreadyProcessedException(String message) {
        super(message);
    }
}