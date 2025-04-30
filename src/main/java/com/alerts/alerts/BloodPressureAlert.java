package com.alerts.alerts;

import com.alerts.Alert;

public class BloodPressureAlert extends Alert {
    private int systolic;
    private int diastolic;

    public BloodPressureAlert(String patientId, String condition, long timestamp, int systolic, int diastolic) {
        super(patientId, condition, timestamp);
        this.systolic = systolic;
        this.diastolic = diastolic;
    }

    public int getSystolic() {
        return systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }
}
