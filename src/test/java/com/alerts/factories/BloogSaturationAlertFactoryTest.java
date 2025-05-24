package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.alerts.BloodSaturationAlert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BloodSaturationAlertFactoryTest {

    @Test
    void testCreateAlert() {
        // Given
        double saturationLevel = 87.5;
        BloodSaturationAlertFactory factory = new BloodSaturationAlertFactory(saturationLevel);

        String patientId = "BSF001";
        String condition = "Low Oxygen";
        long timestamp = System.currentTimeMillis();

        // When
        Alert alert = factory.createAlert(patientId, condition, timestamp);

        // Then
        assertNotNull(alert, "Factory should return a non-null alert");
        assertTrue(alert instanceof BloodSaturationAlert, "Alert should be instance of BloodSaturationAlert");

        BloodSaturationAlert bsAlert = (BloodSaturationAlert) alert;
        assertEquals(patientId, bsAlert.getPatientId(), "Patient ID should match");
        assertEquals(condition, bsAlert.getCondition(), "Condition should match");
        assertEquals(timestamp, bsAlert.getTimestamp(), "Timestamp should match");
        assertEquals(saturationLevel, bsAlert.getSaturationLevel(), 0.001, "Saturation level should match");
    }
}
