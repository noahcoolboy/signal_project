package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.alerts.EcgAlert;

/**
 * Factory class for creating ECG alerts.
 * This factory is responsible for creating ECG alerts with specific heart rate values.
 * It follows the Factory Method design pattern to encapsulate alert creation logic.
 */
public class EcgAlertFactory extends AlertFactory {
    private final double heartRate;

    /**
     * Constructs a new ECG alert factory with the specified heart rate.
     *
     * @param heartRate The heart rate value to be used in created alerts
     */
    public EcgAlertFactory(double heartRate) {
        this.heartRate = heartRate;
    }

    /**
     * Creates a new ECG alert with the specified parameters and the heart rate
     * value provided during factory construction.
     *
     * @param patientId The unique identifier of the patient
     * @param condition The specific ECG condition detected
     * @param timestamp The time when the alert was generated
     * @return A new ECG alert instance
     */
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new EcgAlert(patientId, condition, timestamp, heartRate);
    }
}
