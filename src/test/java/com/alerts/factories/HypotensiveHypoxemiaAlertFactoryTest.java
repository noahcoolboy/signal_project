package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.alerts.HypotensiveHypoxemiaAlert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HypotensiveHypoxemiaAlertFactoryTest {

    @Test
    void testCreateAlert() {
        // Given
        int systolic = 85;
        int diastolic = 55;
        double saturationLevel = 89.2;
        HypotensiveHypoxemiaAlertFactory factory = new HypotensiveHypoxemiaAlertFactory(systolic, diastolic, saturationLevel);

        String patientId = "HHX001";
        String condition = "Critical Hypotensive-Hypoxemia";
        long timestamp = System.currentTimeMillis();

        // When
        Alert alert = factory.createAlert(patientId, condition, timestamp);

        // Then
        assertNotNull(alert, "Factory should produce a non-null alert");
        assertTrue(alert instanceof HypotensiveHypoxemiaAlert, "Alert should be of type HypotensiveHypoxemiaAlert");

        HypotensiveHypoxemiaAlert hhAlert = (HypotensiveHypoxemiaAlert) alert;
        assertEquals(patientId, hhAlert.getPatientId());
        assertEquals(condition, hhAlert.getCondition());
        assertEquals(timestamp, hhAlert.getTimestamp());
        assertEquals(systolic, hhAlert.getSystolic());
        assertEquals(diastolic, hhAlert.getDiastolic());
        assertEquals(saturationLevel, hhAlert.getSaturationLevel(), 0.001);
    }
}
