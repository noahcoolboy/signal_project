package com.alerts.alerts;

import com.alerts.Alert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EcgAlertTest {

    @Test
    void testConstructorAndGetters() {
        // Given
        String patientId = "ECG001";
        String condition = "Tachycardia";
        long timestamp = System.currentTimeMillis();
        double heartRate = 120.7;

        // When
        EcgAlert alert = new EcgAlert(patientId, condition, timestamp, heartRate);

        // Then
        assertEquals(patientId, alert.getPatientId(), "Patient ID should match");
        assertEquals(condition, alert.getCondition(), "Condition should match");
        assertEquals(timestamp, alert.getTimestamp(), "Timestamp should match");
        assertEquals(heartRate, alert.getHeartRate(), 0.001, "Heart rate should match");
    }

    @Test
    void testInheritanceFromAlert() {
        EcgAlert alert = new EcgAlert("ECG002", "Bradycardia", 123456789L, 45.5);
        assertTrue(alert instanceof Alert, "EcgAlert should be an instance of Alert");
    }
}
