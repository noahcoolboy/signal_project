package com.alerts.strategies;

import java.util.List;

import com.data_management.Patient;
import com.data_management.PatientRecord;

public class EcgAlertStrategy implements AlertStrategy {

    private static final int WINDOW_SIZE = 5;
    private static final double THRESHOLD_MULTIPLIER = 1.5;

    @Override
    public String evaluate(Patient patient) {
        List<PatientRecord> records = patient.getRecords("ECG", 20);
        if (records.size() < WINDOW_SIZE) {
            return null; // not enough data
        }

        for (int i = WINDOW_SIZE; i < records.size(); i++) {
            double sum = 0;
            for (int j = i - WINDOW_SIZE; j < i; j++) {
                sum += records.get(j).getMeasurementValue();
            }
            double average = sum / WINDOW_SIZE;
            double currentValue = records.get(i).getMeasurementValue();

            if (currentValue > average * THRESHOLD_MULTIPLIER) {
                return "ECG abnormality detected";
            }
        }

        return null;
    }
}
