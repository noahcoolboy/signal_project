package com.alerts.strategies;

import java.util.List;

import com.alerts.Alert;
import com.alerts.factories.BloodSaturationAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * Strategy for detecting blood oxygen saturation abnormalities and generating appropriate alerts.
 * This class implements the AlertStrategy interface to provide specific logic
 * for monitoring blood oxygen saturation levels.
 * It checks for values below normal range and for rapid drops in saturation.
 */
public class BloodSaturationAlertStrategy implements AlertStrategy {

    /**
     * Constructs a new BloodSaturationAlertStrategy with default parameters.
     */
    public BloodSaturationAlertStrategy() {
        // Default constructor
    }

    /**
     * Checks if a blood saturation alert should be generated based on the patient's data.
     * This method analyzes the patient's recent blood oxygen saturation records and generates
     * an alert if an abnormality is detected, such as values below normal range
     * or rapid drops in saturation.
     *
     * @param patient The patient whose blood oxygen saturation data should be analyzed
     * @return A blood saturation alert if an abnormality is detected, or null if no alert is needed
     */
    @Override
    public Alert checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords("Saturation", 60 * 10 + 5); // Blood saturation is given every second, so we need 10 minutes of data
        if (records.isEmpty()) {
            return null;
        }

        double latestValue = records.get(records.size() - 1).getMeasurementValue();

        if (latestValue < 92.0) {
            return new BloodSaturationAlertFactory(latestValue)
                .createAlert(String.valueOf(patient.getPatientId()), "Oxygen Saturation is too low", System.currentTimeMillis());
        }

        // Check for rapid drop (5%+ drop in 10-minute window)
        if (records.size() >= 60*10+5) {
            PatientRecord latest = records.get(records.size() - 1);
            for (int i = records.size() - 2; i >= 0; i--) {
                PatientRecord previous = records.get(i);
                long timeDiff = latest.getTimestamp() - previous.getTimestamp();
                double valueDiff = previous.getMeasurementValue() - latest.getMeasurementValue();

                if (timeDiff <= 10 * 60 * 1000 && valueDiff >= 5.0) {
                    return new BloodSaturationAlertFactory(latestValue)
                        .createAlert(String.valueOf(patient.getPatientId()), "Oxygen Saturation dropped rapidly", System.currentTimeMillis());
                }
            }
        }

        return null;
    }
}
