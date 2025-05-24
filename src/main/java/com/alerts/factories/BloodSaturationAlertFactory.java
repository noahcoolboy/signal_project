package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.alerts.BloodSaturationAlert;

/**
 * Factory class for creating blood oxygen saturation alerts.
 * This factory is responsible for creating blood saturation alerts with specific saturation levels.
 * It follows the Factory Method design pattern to encapsulate alert creation logic.
 */
public class BloodSaturationAlertFactory extends AlertFactory {
    private final double saturationLevel;

    /**
     * Constructs a new BloodSaturationAlertFactory with the specified saturation level.
     *
     * @param saturationLevel The blood oxygen saturation level to be used in created alerts
     */
    public BloodSaturationAlertFactory(double saturationLevel) {
        this.saturationLevel = saturationLevel;
    }

    /**
     * Creates a new BloodSaturationAlert with the specified parameters and the saturation
     * level provided during factory construction.
     *
     * @param patientId The unique identifier of the patient
     * @param condition The specific blood saturation condition detected
     * @param timestamp The time when the alert was generated
     * @return A new BloodSaturationAlert instance
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new BloodSaturationAlert(patientId, condition, timestamp, saturationLevel);
    }
}
