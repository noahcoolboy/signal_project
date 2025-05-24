package com.alerts.alerts;

import com.alerts.Alert;

/**
 * Alert class for combined hypotensive (low blood pressure) and hypoxemia (low blood oxygen) conditions.
 * This class extends the base Alert class to provide specific functionality
 * for monitoring both blood pressure and blood oxygen saturation levels simultaneously.
 */
public class HypotensiveHypoxemiaAlert extends Alert {
    private int systolic;
    private int diastolic;
    private double saturationLevel;

    /**
     * Constructs a new HypotensiveHypoxemiaAlert with the specified parameters.
     *
     * @param patientId The unique identifier of the patient
     * @param condition The specific condition detected
     * @param timestamp The time when the alert was generated
     * @param systolic The systolic blood pressure value
     * @param diastolic The diastolic blood pressure value
     * @param saturationLevel The blood oxygen saturation level
     */
    public HypotensiveHypoxemiaAlert(String patientId, String condition, long timestamp, 
            int systolic, int diastolic, double saturationLevel) {
        super(patientId, condition, timestamp);
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.saturationLevel = saturationLevel;
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

    /**
     * Gets the blood oxygen saturation level associated with this alert.
     *
     * @return The blood oxygen saturation level as a percentage
     */
    public double getSaturationLevel() {
        return saturationLevel;
    }
}
