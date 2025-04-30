package com.alerts.factory;

import com.alerts.Alert;
import com.alerts.BloodSaturationAlert;

public class BloodSaturationAlertFactory extends AlertFactory {
    private final double saturationLevel;

    public BloodSaturationAlertFactory(double saturationLevel) {
        this.saturationLevel = saturationLevel;
    }

    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new BloodSaturationAlert(patientId, condition, timestamp, saturationLevel);
    }
}
