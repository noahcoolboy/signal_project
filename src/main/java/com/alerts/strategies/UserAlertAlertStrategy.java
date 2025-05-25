package com.alerts.strategies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alerts.Alert;
import com.alerts.factories.UserAlertFactory;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * Strategy for detecting user-triggered alerts.
 * This class implements the AlertStrategy interface to provide specific logic
 * for monitoring user-initiated alerts, such as when a patient presses an emergency button.
 * It keeps track of the last alert time for each patient to avoid duplicate alerts.
 */
public class UserAlertAlertStrategy implements AlertStrategy {

    /**
     * Constructs a new UserAlertAlertStrategy with default parameters.
     * Initializes the map to track the last alert time for each patient.
     */
    public UserAlertAlertStrategy() {
        // Default constructor
    }

    /** Map to store the last alert time for each patient to avoid duplicate alerts */
    private final Map<Patient, Long> lastAlertTime = new HashMap<>();

    /**
     * Checks if a user alert should be generated based on the patient's data.
     * This method analyzes the patient's records since the last check and generates
     * an alert if a user-triggered alert record is found.
     *
     * @param patient The patient whose data should be analyzed
     * @return A user alert if a user-triggered alert is detected, or null if no alert is needed
     */
    @Override
    public Alert checkAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(lastAlertTime.getOrDefault(patient, 0L) + 1, System.currentTimeMillis());
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
