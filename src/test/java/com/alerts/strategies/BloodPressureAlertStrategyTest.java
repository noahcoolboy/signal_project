package com.alerts.strategies;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.alerts.alerts.BloodPressureAlert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * Test class for the BloodPressureAlertStrategy.
 */
public class BloodPressureAlertStrategyTest {

    @Test
    void testCheckAlertWithNoRecords() {
        BloodPressureAlertStrategy strategy = new BloodPressureAlertStrategy();
        Patient patient = new Patient(12345);
        
        Alert alert = strategy.checkAlert(patient);
        
        assertNull(alert, "No alert should be generated when there are no records");
    }
    
    @Test
    void testCheckAlertWithHighSystolicPressure() {
        BloodPressureAlertStrategy strategy = new BloodPressureAlertStrategy();
        int patientId = 12345;
        Patient patient = new Patient(patientId);
        
        // Add a record with high systolic pressure
        patient.addRecord(190.0, "SystolicPressure", System.currentTimeMillis());
        
        Alert alert = strategy.checkAlert(patient);
        
        assertNotNull(alert, "Alert should be generated for high systolic pressure");
        assertTrue(alert instanceof BloodPressureAlert, "Alert should be a BloodPressureAlert");
        assertEquals(String.valueOf(patientId), alert.getPatientId(), "Alert should have the correct patient ID");
        assertTrue(alert.getCondition().contains("SystolicPressure"), "Alert condition should mention systolic pressure");
        assertTrue(alert.getCondition().contains("too high"), "Alert condition should indicate high pressure");
    }
    
    @Test
    void testCheckAlertWithLowDiastolicPressure() {
        BloodPressureAlertStrategy strategy = new BloodPressureAlertStrategy();
        int patientId = 12345;
        Patient patient = new Patient(patientId);
        
        // Add normal systolic record
        patient.addRecord(120.0, "SystolicPressure", System.currentTimeMillis());
        
        // Add low diastolic record
        patient.addRecord(55.0, "DiastolicPressure", System.currentTimeMillis());
        
        Alert alert = strategy.checkAlert(patient);
        
        assertNotNull(alert, "Alert should be generated for low diastolic pressure");
        assertTrue(alert instanceof BloodPressureAlert, "Alert should be a BloodPressureAlert");
        assertEquals(String.valueOf(patientId), alert.getPatientId(), "Alert should have the correct patient ID");
        assertTrue(alert.getCondition().contains("DiastolicPressure"), "Alert condition should mention diastolic pressure");
        assertTrue(alert.getCondition().contains("too low"), "Alert condition should indicate low pressure");
    }
    
    @Test
    void testCheckAlertWithNormalPressure() {
        BloodPressureAlertStrategy strategy = new BloodPressureAlertStrategy();
        int patientId = 12345;
        Patient patient = new Patient(patientId);
        
        // Add normal systolic record
        patient.addRecord(120.0, "SystolicPressure", System.currentTimeMillis());
        
        // Add normal diastolic record
        patient.addRecord(80.0, "DiastolicPressure", System.currentTimeMillis());
        
        Alert alert = strategy.checkAlert(patient);
        
        assertNull(alert, "No alert should be generated for normal blood pressure");
    }
}
