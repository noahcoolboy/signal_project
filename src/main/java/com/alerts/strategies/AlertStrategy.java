package com.alerts.strategies;

import com.alerts.Alert;
import com.data_management.Patient;

public interface AlertStrategy {
    /**
     * Evaluates the patient's data to determine if an alert condition is met.
     *
     * @param patient the patient to check
     * @return a string if an alert condition is met, null otherwise
     */
    String evaluate(Patient patient);
}
