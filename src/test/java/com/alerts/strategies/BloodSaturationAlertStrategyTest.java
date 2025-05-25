package com.alerts.strategies;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.alerts.alerts.BloodSaturationAlert;
import com.data_management.Patient;

/**
 * Test class for the BloodSaturationAlertStrategy.
 */
public class BloodSaturationAlertStrategyTest {

    @Test
    void testCheckAlertWithNoRecords() {
        BloodSaturationAlertStrategy strategy = new BloodSaturationAlertStrategy();
        Patient patient = new Patient(12345);

        Alert alert = strategy.checkAlert(patient);
        assertNull(alert, "No alert should be generated when there are no records");
    }

    @Test
    void testCheckAlertWithLowSaturation() {
        BloodSaturationAlertStrategy strategy = new BloodSaturationAlertStrategy();
        Patient patient = new Patient(12345);

        patient.addRecord(91.5, "Saturation", System.currentTimeMillis());

        Alert alert = strategy.checkAlert(patient);

        assertNotNull(alert);
        assertTrue(alert instanceof BloodSaturationAlert);
        assertTrue(alert.getCondition().contains("too low"));
    }

    @Test
    void testCheckAlertWithNormalSaturation() {
        BloodSaturationAlertStrategy strategy = new BloodSaturationAlertStrategy();
        Patient patient = new Patient(12345);

        patient.addRecord(98.0, "Saturation", System.currentTimeMillis());

        Alert alert = strategy.checkAlert(patient);
        assertNull(alert, "No alert should be generated for normal saturation");
    }

    @Test
    void testCheckAlertWithDropLessThanFivePercent() {
        BloodSaturationAlertStrategy strategy = new BloodSaturationAlertStrategy();
        Patient patient = new Patient(12345);

        long now = System.currentTimeMillis();
        patient.addRecord(97.0, "Saturation", now - 9 * 60 * 1000);
        patient.addRecord(94.0, "Saturation", now - 4 * 60 * 1000);
        patient.addRecord(93.0, "Saturation", now);

        Alert alert = strategy.checkAlert(patient);
        assertNull(alert, "No alert should be triggered for drop less than 5%");
    }

    @Test
    void testCheckAlertWithDropOutsideTenMinuteWindow() {
        BloodSaturationAlertStrategy strategy = new BloodSaturationAlertStrategy();
        Patient patient = new Patient(12345);

        long now = System.currentTimeMillis();
        long elevenMinutesAgo = now - 11 * 60 * 1000;

        patient.addRecord(99.0, "Saturation", elevenMinutesAgo); // too far back
        patient.addRecord(94.0, "Saturation", now - 2 * 60 * 1000);
        patient.addRecord(93.5, "Saturation", now);

        Alert alert = strategy.checkAlert(patient);
        assertNull(alert, "No alert should be generated for drop outside 10-minute window");
    }

    @Test
    void testCheckAlertWithRapidSaturationDrop() {
        BloodSaturationAlertStrategy strategy = new BloodSaturationAlertStrategy();
        Patient patient = new Patient(12345);

        long now = System.currentTimeMillis();
        patient.addRecord(98.0, "Saturation", now - 9 * 60 * 1000); // 9 minutes ago
        patient.addRecord(92.5, "Saturation", now); // 5.5% drop within 10 minutes

        Alert alert = strategy.checkAlert(patient);
        
        assertNotNull(alert, "Alert should be generated for rapid drop");
        assertTrue(alert instanceof BloodSaturationAlert);
        assertTrue(alert.getCondition().contains("dropped rapidly"));

        alert = strategy.checkAlert(patient);
        assertNull(alert, "Alert should not be repeated for the same condition within 10 minutes");
    }

}
