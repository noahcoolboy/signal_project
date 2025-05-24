package com.alerts.factories;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.alerts.alerts.BloodPressureAlert;

/**
 * Test class for the BloodPressureAlertFactory.
 */
public class BloodPressureAlertFactoryTest {

    @Test
    void testCreateAlert() {
        int systolic = 180;
        int diastolic = 110;
        String patientId = "12345";
        String condition = "High Blood Pressure";
        long timestamp = System.currentTimeMillis();
        
        BloodPressureAlertFactory factory = new BloodPressureAlertFactory(systolic, diastolic);
        
        Alert alert = factory.createAlert(patientId, condition, timestamp);
        
        assertNotNull(alert, "Alert should not be null");
        assertTrue(alert instanceof BloodPressureAlert, "Alert should be an instance of BloodPressureAlert");
        
        BloodPressureAlert bpAlert = (BloodPressureAlert) alert;
        assertEquals(patientId, bpAlert.getPatientId(), "Patient ID should match the provided value");
        assertEquals(condition, bpAlert.getCondition(), "Condition should match the provided value");
        assertEquals(timestamp, bpAlert.getTimestamp(), "Timestamp should match the provided value");
        assertEquals(systolic, bpAlert.getSystolic(), "Systolic value should match the factory's value");
        assertEquals(diastolic, bpAlert.getDiastolic(), "Diastolic value should match the factory's value");
    }
}
