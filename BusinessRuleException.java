package com.enviro.assessment.junior.nomdumiso.exception;

/** Thrown when a withdrawal request breaks one of Enviro365's business rules. */
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
