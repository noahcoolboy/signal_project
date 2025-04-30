package com.alerts.strategies;

import java.util.List;

import com.alerts.Alert;
import com.alerts.factory.HypotensiveHypoxemiaAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class HypotensiveHypoxemiaAlertStrategy implements AlertStrategy {

    @Override
    public Alert evaluate(Patient patient) {
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
