package com.enviro.assessment.junior.nomdumiso.exception;

/** Thrown when a requested Investor, Portfolio or Product does not exist. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
