package com.alerts.strategies;

import com.alerts.Alert;
import com.data_management.Patient;

public interface AlertStrategy {
    /**
     * Evaluates the patient's condition and returns an appropriate Alert if needed.
     * @param patient the patient to evaluate
     * @return an Alert if a condition is detected, null otherwise
     */
    Alert evaluate(Patient patient);
}
