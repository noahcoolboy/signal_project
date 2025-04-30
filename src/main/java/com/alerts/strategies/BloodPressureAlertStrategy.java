package com.alerts.strategies;

import java.util.List;

import com.alerts.Alert;
import com.alerts.factory.BloodPressureAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class BloodPressureAlertStrategy implements AlertStrategy {

    @Override
    public Alert evaluate(Patient patient) {
        int systolic = -1;
        int diastolic = -1;
        String condition = null;

        for(String type : new String[] { "SystolicPressure", "DiastolicPressure" }) {
            List<PatientRecord> records = patient.getRecords(type, 3);
            if(records.isEmpty())
                continue;
            
            // Check for range violations
            double value = records.getLast().getMeasurementValue();
            if(type.equals("SystolicPressure")) {
                systolic = (int) value;
                if(value < 90)
                    condition = type + " is too low";
                else if(value > 180)
                    condition = type + " is too high";
            } else if(type.equals("DiastolicPressure")) {
                diastolic = (int) value;
                if(value < 60)
                    condition = type + " is too low";
                else if(value > 120)
                    condition = type + " is too high";
            }

            if (condition != null) {
                return new BloodPressureAlertFactory(systolic, diastolic)
                    .createAlert(String.valueOf(patient.getPatientId()), condition, System.currentTimeMillis());
            }

            // Check for consistent increase / decrease
            double prev = records.get(0).getMeasurementValue();
            boolean increaseFlag = true;
            boolean decreaseFlag = true;
            for (int i = 1; i < records.size(); i++) {
                double current = records.get(i).getMeasurementValue();
                if (prev - current <= 10) {
                    decreaseFlag = false;
                } else if (current - prev <= 10) {
                    increaseFlag = false;
                }
                prev = current;
            }
            
            if(increaseFlag) {
                condition = type + " is increasing rapidly";
            } else if(decreaseFlag) {
                condition = type + " is decreasing rapidly";
            }

            if (condition != null) {
                return new BloodPressureAlertFactory(systolic, diastolic)
                    .createAlert(String.valueOf(patient.getPatientId()), condition, System.currentTimeMillis());
            }
        }
        return null;
    }
}
