package com.alerts.strategies;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.alerts.alerts.BloodPressureAlert;
import com.data_management.Patient;

/**
 * Extended test class for the BloodPressureAlertStrategy.
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

        patient.addRecord(190.0, "SystolicPressure", System.currentTimeMillis());

        Alert alert = strategy.checkAlert(patient);

        assertNotNull(alert);
        assertTrue(alert instanceof BloodPressureAlert);
        assertTrue(alert.getCondition().contains("too high"));
    }

    @Test
    void testCheckAlertWithLowSystolicPressure() {
        BloodPressureAlertStrategy strategy = new BloodPressureAlertStrategy();
        Patient patient = new Patient(12345);
        patient.addRecord(85.0, "SystolicPressure", System.currentTimeMillis());

        Alert alert = strategy.checkAlert(patient);

        assertNotNull(alert);
        assertTrue(alert.getCondition().contains("SystolicPressure"));
        assertTrue(alert.getCondition().contains("too low"));
    }

    @Test
    void testCheckAlertWithHighDiastolicPressure() {
        BloodPressureAlertStrategy strategy = new BloodPressureAlertStrategy();
        Patient patient = new Patient(12345);
        patient.addRecord(125.0, "DiastolicPressure", System.currentTimeMillis());

        Alert alert = strategy.checkAlert(patient);

        assertNotNull(alert);
        assertTrue(alert.getCondition().contains("DiastolicPressure"));
        assertTrue(alert.getCondition().contains("too high"));
    }

    @Test
    void testCheckAlertWithLowDiastolicPressure() {
        BloodPressureAlertStrategy strategy = new BloodPressureAlertStrategy();
        Patient patient = new Patient(12345);

        patient.addRecord(120.0, "SystolicPressure", System.currentTimeMillis());
        patient.addRecord(55.0, "DiastolicPressure", System.currentTimeMillis());

        Alert alert = strategy.checkAlert(patient);

        assertNotNull(alert);
        assertTrue(alert.getCondition().contains("DiastolicPressure"));
        assertTrue(alert.getCondition().contains("too low"));
    }

    @Test
    void testCheckAlertWithNormalPressure() {
        BloodPressureAlertStrategy strategy = new BloodPressureAlertStrategy();
        Patient patient = new Patient(12345);

        patient.addRecord(120.0, "SystolicPressure", System.currentTimeMillis());
        patient.addRecord(80.0, "DiastolicPressure", System.currentTimeMillis());

        Alert alert = strategy.checkAlert(patient);

        assertNull(alert, "No alert should be generated for normal blood pressure");
    }

    @Test
    void testOnlyLastThreeRecordsUsed() {
        BloodPressureAlertStrategy strategy = new BloodPressureAlertStrategy();
        Patient patient = new Patient(12345);

        // First record is abnormal, but should be ignored if it's more than 3 records ago
        patient.addRecord(250.0, "SystolicPressure", System.currentTimeMillis() - 40000);
        patient.addRecord(120.0, "SystolicPressure", System.currentTimeMillis() - 30000);
        patient.addRecord(122.0, "SystolicPressure", System.currentTimeMillis() - 20000);
        patient.addRecord(121.0, "SystolicPressure", System.currentTimeMillis() - 10000);

        Alert alert = strategy.checkAlert(patient);
        assertNull(alert, "Should not generate alert if last 3 records are normal");
    }
}
