package com.alerts.factory;

import com.alerts.Alert;

/**
 * Base factory class for creating alerts. Subclasses should implement the createAlert
 * method to return specific types of alerts.
 */
public abstract class AlertFactory {
    /**
     * Creates an alert of a specific type based on the implementation.
     * 
     * @param patientId the ID of the patient associated with the alert
     * @param condition the condition which has triggered the alert
     * @param timestamp the time at which the alert was generated
     * @return an Alert instance of the appropriate type
     */
    public abstract Alert createAlert(String patientId, String condition, long timestamp);
}
