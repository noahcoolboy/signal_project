package com.alerts.alerts;

import com.alerts.Alert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserAlertTest {

    @Test
    void testConstructorAndGetters() {
        // Given
        String patientId = "USR001";
        String condition = "Custom Alert";
        long timestamp = System.currentTimeMillis();
        String message = "Patient reported severe headache";

        // When
        UserAlert alert = new UserAlert(patientId, condition, timestamp, message);

        // Then
        assertEquals(patientId, alert.getPatientId(), "Patient ID should match");
        assertEquals(condition, alert.getCondition(), "Condition should match");
        assertEquals(timestamp, alert.getTimestamp(), "Timestamp should match");
        assertEquals(message, alert.getMessage(), "Custom message should match");
    }

    @Test
    void testInheritanceFromAlert() {
        UserAlert alert = new UserAlert("USR002", "Emergency", 123456789L, "Severe dizziness reported");
        assertTrue(alert instanceof Alert, "UserAlert should inherit from Alert");
    }
}
