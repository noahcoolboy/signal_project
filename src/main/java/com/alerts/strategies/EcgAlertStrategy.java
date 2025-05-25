package com.alerts.strategies;

import java.util.List;
import com.alerts.factories.*;
import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * Strategy for detecting ECG abnormalities and generating appropriate alerts.
 * This class implements the AlertStrategy interface to provide specific logic
 * for monitoring ECG data and identifying potential cardiac issues.
 * It uses a sliding window approach to analyze ECG patterns and detect anomalies.
 */
public class EcgAlertStrategy implements AlertStrategy {

    /**
     * Constructs a new EcgAlertStrategy with default parameters.
     */
    public EcgAlertStrategy() {
        // Default constructor
    }

    /** The size of the sliding window used for ECG data analysis (in data points) */
    private static final int WINDOW_SIZE = 60;
    
    /** The multiplier used to determine the threshold for abnormal ECG values */
    private static final double THRESHOLD = 1;

    /**
     * Checks if an ECG alert should be generated based on the patient's ECG data.
     * This method analyzes the patient's recent ECG records using a sliding window approach
     * and generates an alert if an abnormality is detected.
     *
     * @param patient The patient whose ECG data should be analyzed
     * @return An ECG alert if an abnormality is detected, or null if no alert is needed
     */
    @Override
    public Alert checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords("ECG", 61); // 1 minute of ECG data
        if (records.size() < WINDOW_SIZE + 1) {
            return null; // not enough data
        }

        // Calculate heart rate from ECG data (simplified)
        double heartRate = records.get(records.size() - 1).getMeasurementValue();

        for (int i = WINDOW_SIZE; i < records.size(); i++) {
            double sum = 0;
            for (int j = i - WINDOW_SIZE; j < i; j++) {
                sum += records.get(j).getMeasurementValue();
            }
            double average = sum / WINDOW_SIZE;
            double currentValue = records.get(i).getMeasurementValue();
            if (Math.abs(currentValue - average) > THRESHOLD) {
                return new EcgAlertFactory(heartRate)
                    .createAlert(String.valueOf(patient.getPatientId()), 
                               "ECG abnormality detected", 
                               System.currentTimeMillis());
            }
        }

        return null;
    }
}
