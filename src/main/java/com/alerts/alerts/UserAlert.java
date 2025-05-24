package com.alerts.alerts;

import com.alerts.Alert;

/**
 * Alert class for user-defined alerts.
 * This class extends the base Alert class to provide functionality
 * for custom alerts with user-specified messages.
 */
public class UserAlert extends Alert {
    private String message;

    /**
     * Constructs a new UserAlert with the specified parameters.
     *
     * @param patientId The unique identifier of the patient
     * @param condition The specific condition or reason for the alert
     * @param timestamp The time when the alert was generated
     * @param message The custom message for this alert
     */
    public UserAlert(String patientId, String condition, long timestamp, String message) {
        super(patientId, condition, timestamp);
        this.message = message;
    }

    /**
     * Gets the custom message associated with this alert.
     *
     * @return The user-defined message
     */
    public String getMessage() {
        return message;
    }
}
