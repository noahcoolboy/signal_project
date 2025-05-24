package com.alerts.alerts;

import com.alerts.Alert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BloodSaturationAlertTest {

    @Test
    void testConstructorAndGetters() {
        // Given
        String patientId = "11523";
        String condition = "Low Saturation";
        long timestamp = System.currentTimeMillis();
        double saturationLevel = 88.5;

        // When
        BloodSaturationAlert alert = new BloodSaturationAlert(patientId, condition, timestamp, saturationLevel);

        // Then
        assertEquals(patientId, alert.getPatientId(), "Patient ID should match");
        assertEquals(condition, alert.getCondition(), "Condition should match");
        assertEquals(timestamp, alert.getTimestamp(), "Timestamp should match");
        assertEquals(saturationLevel, alert.getSaturationLevel(), 0.001, "Saturation level should match");
    }

    @Test
    void testInheritanceFromAlert() {
        BloodSaturationAlert alert = new BloodSaturationAlert("P456", "Critical", 123456789L, 75.0);

        assertTrue(alert instanceof Alert, "BloodSaturationAlert should be an instance of Alert");
    }
}
