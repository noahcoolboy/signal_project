package com.alerts.strategies;

import java.util.List;

import com.alerts.Alert;
import com.alerts.factory.EcgAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class EcgAlertStrategy implements AlertStrategy {

    private static final int WINDOW_SIZE = 5;
    private static final double THRESHOLD_MULTIPLIER = 1.5;

    @Override
    public Alert evaluate(Patient patient) {
        List<PatientRecord> records = patient.getRecords("ECG", 20);
        if (records.size() < WINDOW_SIZE) {
            return null; // not enough data
        }

        // Calculate heart rate from ECG data (simplified)
        double heartRate = records.getLast().getMeasurementValue(); // TODO

        for (int i = WINDOW_SIZE; i < records.size(); i++) {
            double sum = 0;
            for (int j = i - WINDOW_SIZE; j < i; j++) {
                sum += records.get(j).getMeasurementValue();
            }
            double average = sum / WINDOW_SIZE;
            double currentValue = records.get(i).getMeasurementValue();

            if (currentValue > average * THRESHOLD_MULTIPLIER) {
                return new EcgAlertFactory(heartRate)
                    .createAlert(String.valueOf(patient.getPatientId()), 
                               "ECG abnormality detected", 
                               System.currentTimeMillis());
            }
        }

        return null;
    }
}
