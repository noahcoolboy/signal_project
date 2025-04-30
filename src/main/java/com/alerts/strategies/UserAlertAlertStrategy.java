package com.alerts.strategies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alerts.Alert;
import com.alerts.factories.UserAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class UserAlertAlertStrategy implements AlertStrategy {

    private final Map<Patient, Long> lastAlertTime = new HashMap<>();

    @Override
    public Alert checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(lastAlertTime.getOrDefault(patient, 0L), System.currentTimeMillis());
        lastAlertTime.put(patient, System.currentTimeMillis());
        
        for (PatientRecord record : records) {
            if(record.getRecordType().equals("Alert") && record.getMeasurementValue() == 0) { // 0 is the "triggered" state
                // Get additional alert details from the record
                String message = "Alert triggered by user";
                
                return new UserAlertFactory(message)
                    .createAlert(String.valueOf(patient.getPatientId()),
                               "User Alert triggered",
                               System.currentTimeMillis());
            }
        }
        return null;
    }
}
