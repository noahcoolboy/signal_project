package com.alerts.strategies;

import java.util.List;

import com.data_management.Patient;
import com.data_management.PatientRecord;

public class BloodPressureAlertStrategy implements AlertStrategy {

    @Override
    public String evaluate(Patient patient) {
        for(String type : new String[] { "SystolicPressure", "DiastolicPressure" }) {
            List<PatientRecord> records = patient.getRecords(type, 3);
            if(records.isEmpty())
                continue;
            
            // Check for range violations
            double value = records.getLast().getMeasurementValue();
            if(type == "SystolicPressure") {
                if(value < 90)
                    return type + " is too low";
                else if(value > 180)
                    return type + " is too high";
            } else if(type == "DiastolicPressure") {
                if(value < 60)
                    return type + " is too low";
                else if(value > 120)
                    return type + " is too high";
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
                return type + " is increasing rapidly";
            } else if(decreaseFlag) {
                return type + " is decreasing rapidly";
            }
        }
        return null;
    }
    
}
