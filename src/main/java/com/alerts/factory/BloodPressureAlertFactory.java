package com.alerts.factory;

import com.alerts.Alert;
import com.alerts.BloodPressureAlert;

public class BloodPressureAlertFactory extends AlertFactory {
    private final int systolic;
    private final int diastolic;

    public BloodPressureAlertFactory(int systolic, int diastolic) {
        this.systolic = systolic;
        this.diastolic = diastolic;
    }

    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new BloodPressureAlert(patientId, condition, timestamp, systolic, diastolic);
    }
}
