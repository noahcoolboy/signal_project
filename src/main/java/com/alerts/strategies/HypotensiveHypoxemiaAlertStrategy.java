package com.alerts.strategies;

import java.util.List;

import com.data_management.Patient;
import com.data_management.PatientRecord;

public class HypotensiveHypoxemiaAlertStrategy implements AlertStrategy {

    @Override
    public String evaluate(Patient patient) {
        List<PatientRecord> bpRecords = patient.getRecords("SystolicPressure", 1);
        if (bpRecords.isEmpty()) {
            return null;
        }
        double systolic = bpRecords.get(0).getMeasurementValue();

        List<PatientRecord> saturationRecords = patient.getRecords("Saturation", 1);
        if (saturationRecords.isEmpty()) {
            return null;
        }
        double saturation = saturationRecords.get(0).getMeasurementValue();

        if (systolic < 90.0 && saturation < 92.0) {
            return "Hypotensive Hypoxemia detected";
        }

        return null;
    }
}
