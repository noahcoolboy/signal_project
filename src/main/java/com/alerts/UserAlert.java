package com.alerts;

public class UserAlert extends Alert {
    private String message;

    public UserAlert(String patientId, String condition, long timestamp, String message) {
        super(patientId, condition, timestamp);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
