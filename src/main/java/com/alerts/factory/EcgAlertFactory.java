package com.alerts.factory;

import com.alerts.Alert;
import com.alerts.alerts.EcgAlert;

public class EcgAlertFactory extends AlertFactory {
    private final double heartRate;

    public EcgAlertFactory(double heartRate) {
        this.heartRate = heartRate;
    }

    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new EcgAlert(patientId, condition, timestamp, heartRate);
    }
}
