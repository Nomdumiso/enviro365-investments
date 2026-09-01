package com.enviro.assessment.junior.nomdumiso.exception;

import java.time.LocalDateTime;
import java.util.List;

/** Consistent error shape returned to the frontend for every failure case. */
public class ApiError {

    private LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String error;
    private String message;
    private List<String> details;

    public ApiError() {
    }

    public ApiError(int status, String error, String message, List<String> details) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public List<String> getDetails() { return details; }
    public void setDetails(List<String> details) { this.details = details; }
}
