package com.alerts.strategies;

import java.util.List;

import com.alerts.Alert;
import com.alerts.factories.EcgAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class EcgAlertStrategy implements AlertStrategy {

    private static final int WINDOW_SIZE = 60;
    private static final double THRESHOLD_MULTIPLIER = 2;

    @Override
    public Alert checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords("ECG", 60); // 1 minute of ECG data
        if (records.size() < WINDOW_SIZE) {
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

            if (Math.abs(currentValue / average) > THRESHOLD_MULTIPLIER) {
                System.out.println(currentValue);
                System.out.println(average * THRESHOLD_MULTIPLIER);
                return new EcgAlertFactory(heartRate)
                    .createAlert(String.valueOf(patient.getPatientId()), 
                               "ECG abnormality detected", 
                               System.currentTimeMillis());
            }
        }

        return null;
    }
}
