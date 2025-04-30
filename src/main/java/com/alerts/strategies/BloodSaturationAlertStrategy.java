package com.alerts.strategies;

import java.util.List;

import com.data_management.Patient;
import com.data_management.PatientRecord;

public class BloodSaturationAlertStrategy implements AlertStrategy {

    @Override
    public String evaluate(Patient patient) {
        List<PatientRecord> records = patient.getRecords("Saturation", 60 * 10 + 5); // Blood saturation is given every second, so we need 10 minutes of data
        if (records.isEmpty()) {
            return null;
        }

        double latestValue = records.getLast().getMeasurementValue();
        if (latestValue < 92.0) {
            return "Oxygen Saturation is too low";
        }

        // Check for rapid drop (5%+ drop in 10-minute window)
        if (records.size() >= 2) {
            PatientRecord latest = records.getLast();
            for (int i = records.size() - 2; i >= 0; i--) {
                PatientRecord previous = records.get(i);
                long timeDiff = latest.getTimestamp() - previous.getTimestamp();
                double valueDiff = previous.getMeasurementValue() - latest.getMeasurementValue();

                if (timeDiff <= 10 * 60 * 1000 && valueDiff >= 5.0) {
                    return "Oxygen Saturation dropped rapidly";
                }
            }
        }

        return null;
    }
}
