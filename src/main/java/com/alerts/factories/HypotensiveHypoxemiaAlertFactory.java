package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.alerts.HypotensiveHypoxemiaAlert;

/**
 * Factory class for creating combined hypotensive and hypoxemia alerts.
 * This factory is responsible for creating alerts that monitor both blood pressure
 * and blood oxygen saturation levels simultaneously.
 * It follows the Factory Method design pattern to encapsulate alert creation logic.
 */
public class HypotensiveHypoxemiaAlertFactory extends AlertFactory {
    private final int systolic;
    private final int diastolic;
    private final double saturationLevel;

    /**
     * Constructs a new HypotensiveHypoxemiaAlertFactory with the specified parameters.
     *
     * @param systolic The systolic blood pressure value to be used in created alerts
     * @param diastolic The diastolic blood pressure value to be used in created alerts
     * @param saturationLevel The blood oxygen saturation level to be used in created alerts
     */
    public HypotensiveHypoxemiaAlertFactory(int systolic, int diastolic, double saturationLevel) {
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.saturationLevel = saturationLevel;
    }

    /**
     * Creates a new HypotensiveHypoxemiaAlert with the specified parameters and the values
     * provided during factory construction.
     *
     * @param patientId The unique identifier of the patient
     * @param condition The specific condition detected
     * @param timestamp The time when the alert was generated
     * @return A new HypotensiveHypoxemiaAlert instance
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new HypotensiveHypoxemiaAlert(patientId, condition, timestamp, 
            systolic, diastolic, saturationLevel);
    }
}
