package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.alerts.UserAlert;

public class UserAlertFactory extends AlertFactory {
    private final String message;

    public UserAlertFactory(String message) {
        this.message = message;
    }

    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new UserAlert(patientId, condition, timestamp, message);
    }
}
