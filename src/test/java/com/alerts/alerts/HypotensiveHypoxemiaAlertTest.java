package com.alerts.alerts;

import com.alerts.Alert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HypotensiveHypoxemiaAlertTest {

    @Test
    void testConstructorAndGetters() {
        // Given
        String patientId = "HHX001";
        String condition = "Hypotensive Hypoxemia";
        long timestamp = System.currentTimeMillis();
        int systolic = 85;
        int diastolic = 55;
        double saturationLevel = 89.2;

        // When
        HypotensiveHypoxemiaAlert alert = new HypotensiveHypoxemiaAlert(
                patientId, condition, timestamp, systolic, diastolic, saturationLevel);

        // Then
        assertEquals(patientId, alert.getPatientId(), "Patient ID should match");
        assertEquals(condition, alert.getCondition(), "Condition should match");
        assertEquals(timestamp, alert.getTimestamp(), "Timestamp should match");
        assertEquals(systolic, alert.getSystolic(), "Systolic value should match");
        assertEquals(diastolic, alert.getDiastolic(), "Diastolic value should match");
        assertEquals(saturationLevel, alert.getSaturationLevel(), 0.001, "Saturation level should match");
    }

    @Test
    void testInheritanceFromAlert() {
        HypotensiveHypoxemiaAlert alert = new HypotensiveHypoxemiaAlert(
                "HHX002", "Critical", 123456789L, 90, 60, 91.0);
        assertTrue(alert instanceof Alert, "HypotensiveHypoxemiaAlert should be an instance of Alert");
    }
}
