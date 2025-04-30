package com.alerts.alerts;

import com.alerts.Alert;

public class BloodSaturationAlert extends Alert {
    private double saturationLevel;

    public BloodSaturationAlert(String patientId, String condition, long timestamp, double saturationLevel) {
        super(patientId, condition, timestamp);
        this.saturationLevel = saturationLevel;
    }

    public double getSaturationLevel() {
        return saturationLevel;
    }
}
