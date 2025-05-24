package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.alerts.UserAlert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserAlertFactoryTest {

    @Test
    void testCreateAlert() {
        // Given
        String customMessage = "Patient requested assistance";
        UserAlertFactory factory = new UserAlertFactory(customMessage);

        String patientId = "USR101";
        String condition = "Manual Alert";
        long timestamp = System.currentTimeMillis();

        // When
        Alert alert = factory.createAlert(patientId, condition, timestamp);

        // Then
        assertNotNull(alert, "Factory should produce a non-null alert");
        assertTrue(alert instanceof UserAlert, "Alert should be of type UserAlert");

        UserAlert userAlert = (UserAlert) alert;
        assertEquals(patientId, userAlert.getPatientId());
        assertEquals(condition, userAlert.getCondition());
        assertEquals(timestamp, userAlert.getTimestamp());
        assertEquals(customMessage, userAlert.getMessage());
    }
}
