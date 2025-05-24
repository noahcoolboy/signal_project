package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.alerts.BloodPressureAlert;

/**
 * Factory class for creating blood pressure alerts.
 * This factory is responsible for creating blood pressure alerts with specific systolic and diastolic values.
 * It follows the Factory Method design pattern to encapsulate alert creation logic.
 */
public class BloodPressureAlertFactory extends AlertFactory {
    private final int systolic;
    private final int diastolic;

    /**
     * Constructs a new BloodPressureAlertFactory with the specified blood pressure values.
     *
     * @param systolic The systolic blood pressure value to be used in created alerts
     * @param diastolic The diastolic blood pressure value to be used in created alerts
     */
    public BloodPressureAlertFactory(int systolic, int diastolic) {
        this.systolic = systolic;
        this.diastolic = diastolic;
    }

    /**
     * Creates a new BloodPressureAlert with the specified parameters and the blood pressure
     * values provided during factory construction.
     *
     * @param patientId The unique identifier of the patient
     * @param condition The specific blood pressure condition detected
     * @param timestamp The time when the alert was generated
     * @return A new BloodPressureAlert instance
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new BloodPressureAlert(patientId, condition, timestamp, systolic, diastolic);
    }
}
