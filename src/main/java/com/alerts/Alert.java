package com.alerts;

// Represents an alert
/**
 * The {@code Alert} class represents a medical alert for a patient.
 * Alerts are generated when certain conditions are met via the
 * {@link AlertGenerator} class. Basic information about the alert is stored,
 * including the patient ID, the condition that triggered the alert, and the
 * timestamp of when the alert was generated.
 */
public class Alert {
    private String patientId;
    private String condition;
    private long timestamp;

    /**
     * Creates a new Alert instance with the specified patient ID, condition, and
     * timestamp.
     * @param patientId the ID of the patient associated with the alert
     * @param condition the condition which has triggered the alert
     * @param timestamp the time at which the alert was generated
     */
    public Alert(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    /**
     * Gets the patient ID associated with this alert.
     * @return the ID of the patient
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Gets the condition which has triggered the alert.
     * @return the condition that triggered the alert
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Gets the timestamp of when the alert was generated.
     * @return the time at which the alert was generated
     */
    public long getTimestamp() {
        return timestamp;
    }
}
