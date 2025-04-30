package com.alerts.factories;

import com.alerts.Alert;
import com.alerts.alerts.HypotensiveHypoxemiaAlert;

public class HypotensiveHypoxemiaAlertFactory extends AlertFactory {
    private final int systolic;
    private final int diastolic;
    private final double saturationLevel;

    public HypotensiveHypoxemiaAlertFactory(int systolic, int diastolic, double saturationLevel) {
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.saturationLevel = saturationLevel;
    }

    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new HypotensiveHypoxemiaAlert(patientId, condition, timestamp, 
            systolic, diastolic, saturationLevel);
    }
}
