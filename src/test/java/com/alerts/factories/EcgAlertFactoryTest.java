package com.alerts.factories;

import org.junit.jupiter.api.Test;
import com.alerts.Alert;
import com.alerts.alerts.*;

import static org.junit.jupiter.api.Assertions.*;

public class EcgAlertFactoryTest {
    @Test
    void testCreateAlert() {
        // Given
        double heartRate = 150.0; // Example heart rate
        EcgAlertFactory factory = new EcgAlertFactory(heartRate);

        String patientId = "ECG001";
        String condition = "Tachycardia";
        long timestamp = System.currentTimeMillis();

        // When
        Alert alert = factory.createAlert(patientId, condition, timestamp);

        // Then
        assertNotNull(alert, "Factory should return a non-null alert");
        assertTrue(alert instanceof EcgAlert, "Alert should be instance of EcgAlert");

        EcgAlert ecgAlert = (EcgAlert) alert;
        assertEquals(patientId, ecgAlert.getPatientId(), "Patient ID should match");
        assertEquals(condition, ecgAlert.getCondition(), "Condition should match");
        assertEquals(timestamp, ecgAlert.getTimestamp(), "Timestamp should match");
        assertEquals(heartRate, ecgAlert.getHeartRate(), 0.001, "Heart rate should match");
    }
}
