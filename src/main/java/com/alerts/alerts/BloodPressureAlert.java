package com.alerts.alerts;

import com.alerts.Alert;

/**
 * Alert class for blood pressure related alerts.
 * This class extends the base Alert class to provide specific functionality
 * for monitoring blood pressure conditions, including both systolic and diastolic values.
 */
public class BloodPressureAlert extends Alert {
    private int systolic;
    private int diastolic;

    /**
     * Constructs a new BloodPressureAlert with the specified parameters.
     *
     * @param patientId The unique identifier of the patient
     * @param condition The specific blood pressure condition detected
     * @param timestamp The time when the alert was generated
     * @param systolic The systolic blood pressure value
     * @param diastolic The diastolic blood pressure value
     */
    public BloodPressureAlert(String patientId, String condition, long timestamp, int systolic, int diastolic) {
        super(patientId, condition, timestamp);
        this.systolic = systolic;
        this.diastolic = diastolic;
    }

    /**
     * Gets the systolic blood pressure value associated with this alert.
     *
     * @return The systolic blood pressure in mmHg
     */
    public int getSystolic() {
        return systolic;
    }

    /**
     * Gets the diastolic blood pressure value associated with this alert.
     *
     * @return The diastolic blood pressure in mmHg
     */
    public int getDiastolic() {
        return diastolic;
    }
}
