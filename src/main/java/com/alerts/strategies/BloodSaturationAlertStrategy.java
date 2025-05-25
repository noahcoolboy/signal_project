package com.alerts.strategies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    Map<Integer, Long> lastAlertTimestamps = new HashMap<>();

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
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        for (PatientRecord record : records) {
            if(record.getTimestamp() < lastAlertTimestamps.getOrDefault(patient.getPatientId(), 0L) || System.currentTimeMillis() - record.getTimestamp() > 10 * 60 * 1000)
                continue;
            double value = record.getMeasurementValue();
            max = Math.max(max, value);
            min = Math.min(min, value);
        }
        double dropPercentage = max - min;
        if (dropPercentage >= 5.0) {
            lastAlertTimestamps.put(patient.getPatientId(), System.currentTimeMillis());
            return new BloodSaturationAlertFactory(latestValue)
                .createAlert(String.valueOf(patient.getPatientId()), "Oxygen Saturation dropped rapidly", System.currentTimeMillis());
        }

        return null;
    }
}
