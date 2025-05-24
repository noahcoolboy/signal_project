package com.alerts.alerts;

import com.alerts.Alert;

/**
 * Alert class for blood oxygen saturation related alerts.
 * This class extends the base Alert class to provide specific functionality
 * for monitoring blood oxygen saturation levels.
 */
public class BloodSaturationAlert extends Alert {
    private double saturationLevel;

    /**
     * Constructs a new BloodSaturationAlert with the specified parameters.
     *
     * @param patientId The unique identifier of the patient
     * @param condition The specific blood saturation condition detected
     * @param timestamp The time when the alert was generated
     * @param saturationLevel The blood oxygen saturation level
     */
    public BloodSaturationAlert(String patientId, String condition, long timestamp, double saturationLevel) {
        super(patientId, condition, timestamp);
        this.saturationLevel = saturationLevel;
    }

    /**
     * Gets the blood oxygen saturation level associated with this alert.
     *
     * @return The blood oxygen saturation level as a percentage
     */
    public double getSaturationLevel() {
        return saturationLevel;
    }
}
