package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.alerts.UserAlert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlertFactoryTest {

    @Test
    void testCreateAlert() {
        // Arrange
        AlertFactory factory = new UserAlertFactory("Assistance required");
        String patientId = "P-007";
        String condition = "Manual Trigger";
        long timestamp = System.currentTimeMillis();

        // Act
        Alert alert = factory.createAlert(patientId, condition, timestamp);

        // Assert
        assertNotNull(alert);
        assertEquals(patientId, alert.getPatientId());
        assertEquals(condition, alert.getCondition());
        assertEquals(timestamp, alert.getTimestamp());

        assertTrue(alert instanceof UserAlert);
        assertEquals("Assistance required", ((UserAlert) alert).getMessage());
    }
}
