package com.alerts.strategies;

import java.util.List;

import com.alerts.Alert;
import com.alerts.factories.HypotensiveHypoxemiaAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * Strategy for detecting combined hypotensive (low blood pressure) and hypoxemia (low blood oxygen) conditions.
 * This class implements the AlertStrategy interface to provide specific logic
 * for monitoring both blood pressure and blood oxygen saturation levels simultaneously.
 * It generates alerts when both conditions are present at the same time, which can indicate
 * a serious medical emergency.
 */
public class HypotensiveHypoxemiaAlertStrategy implements AlertStrategy {

    /**
     * Constructs a new HypotensiveHypoxemiaAlertStrategy with default parameters.
     */
    public HypotensiveHypoxemiaAlertStrategy() {
        // Default constructor
    }

    /**
     * Checks if a hypotensive hypoxemia alert should be generated based on the patient's data.
     * This method analyzes the patient's recent blood pressure and oxygen saturation records
     * and generates an alert if both hypotension (low blood pressure) and hypoxemia (low blood oxygen)
     * are detected simultaneously.
     *
     * @param patient The patient whose data should be analyzed
     * @return A hypotensive hypoxemia alert if both conditions are detected, or null if no alert is needed
     */
    @Override
    public Alert checkAlert(Patient patient) {
        List<PatientRecord> systolicRecords = patient.getRecords("SystolicPressure", 1);
        List<PatientRecord> diastolicRecords = patient.getRecords("DiastolicPressure", 1);
        if (systolicRecords.isEmpty() || diastolicRecords.isEmpty()) {
            return null;
        }
        int systolic = (int) systolicRecords.get(0).getMeasurementValue();
        int diastolic = (int) diastolicRecords.get(0).getMeasurementValue();

        List<PatientRecord> saturationRecords = patient.getRecords("Saturation", 1);
        if (saturationRecords.isEmpty()) {
            return null;
        }
        double saturation = saturationRecords.get(0).getMeasurementValue();

        if (systolic < 90.0 && saturation < 92.0) {
            return new HypotensiveHypoxemiaAlertFactory(systolic, diastolic, saturation)
                .createAlert(String.valueOf(patient.getPatientId()),
                           "Hypotensive Hypoxemia detected",
                           System.currentTimeMillis());
        }

        return null;
    }
}
