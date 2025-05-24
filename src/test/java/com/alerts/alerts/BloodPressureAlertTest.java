package com.alerts.alerts;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test class for the BloodPressureAlert.
 */
public class BloodPressureAlertTest {

    @Test
    void testBloodPressureAlertConstructorAndGetters() {
        String patientId = "12345";
        String condition = "High Blood Pressure";
        long timestamp = System.currentTimeMillis();
        int systolic = 180;
        int diastolic = 110;
        
        BloodPressureAlert alert = new BloodPressureAlert(patientId, condition, timestamp, systolic, diastolic);
        
        assertEquals(patientId, alert.getPatientId(), "Patient ID should match the constructor argument");
        assertEquals(condition, alert.getCondition(), "Condition should match the constructor argument");
        assertEquals(timestamp, alert.getTimestamp(), "Timestamp should match the constructor argument");
        assertEquals(systolic, alert.getSystolic(), "Systolic value should match the constructor argument");
        assertEquals(diastolic, alert.getDiastolic(), "Diastolic value should match the constructor argument");
    }
}
