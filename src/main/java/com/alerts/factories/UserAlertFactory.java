package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.alerts.UserAlert;

/**
 * Factory class for creating user-defined alerts.
 * This factory is responsible for creating alerts with custom user-specified messages.
 * It follows the Factory Method design pattern to encapsulate alert creation logic.
 */
public class UserAlertFactory extends AlertFactory {
    private final String message;

    /**
     * Constructs a new UserAlertFactory with the specified message.
     *
     * @param message The custom message to be used in created alerts
     */
    public UserAlertFactory(String message) {
        this.message = message;
    }

    /**
     * Creates a new UserAlert with the specified parameters and the message
     * provided during factory construction.
     *
     * @param patientId The unique identifier of the patient
     * @param condition The specific condition or reason for the alert
     * @param timestamp The time when the alert was generated
     * @return A new UserAlert instance
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new UserAlert(patientId, condition, timestamp, message);
    }
}
