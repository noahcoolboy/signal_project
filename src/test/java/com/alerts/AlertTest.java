package com.alerts;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Test class for the Alert base class.
 */
public class AlertTest {

    @Test
    void testAlertConstructorAndGetters() {
        String patientId = "12345";
        String condition = "High Heart Rate";
        long timestamp = System.currentTimeMillis();
        
        Alert alert = new Alert(patientId, condition, timestamp);
        
        assertEquals(patientId, alert.getPatientId(), "Patient ID should match the constructor argument");
        assertEquals(condition, alert.getCondition(), "Condition should match the constructor argument");
        assertEquals(timestamp, alert.getTimestamp(), "Timestamp should match the constructor argument");
    }
}
