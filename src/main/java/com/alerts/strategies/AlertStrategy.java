package com.alerts.strategies;

import com.alerts.Alert;
import com.data_management.Patient;

public interface AlertStrategy {
    /**
     * Checks if an alert should be created for the patient, and if so, creates it. Returns null otherwise.
     * @param patient the patient to evaluate
     * @return an Alert if a condition is detected, null otherwise
     */
    Alert checkAlert(Patient patient);
}
