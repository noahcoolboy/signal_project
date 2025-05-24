package com.alerts.alerts;

import com.alerts.Alert;

/**
 * Alert class for ECG (Electrocardiogram) related alerts.
 * This class extends the base Alert class to provide specific functionality
 * for heart rate monitoring and ECG-related conditions.
 */
public class EcgAlert extends Alert {
    private double heartRate;

    /**
     * Constructs a new ECG alert with the specified parameters.
     *
     * @param patientId The unique identifier of the patient
     * @param condition The specific ECG condition detected
     * @param timestamp The time when the alert was generated
     * @param heartRate The heart rate value that triggered the alert
     */
    public EcgAlert(String patientId, String condition, long timestamp, double heartRate) {
        super(patientId, condition, timestamp);
        this.heartRate = heartRate;
    }

    /**
     * Gets the heart rate value associated with this ECG alert.
     *
     * @return The heart rate in beats per minute (BPM)
     */
    public double getHeartRate() {
        return heartRate;
    }
}
