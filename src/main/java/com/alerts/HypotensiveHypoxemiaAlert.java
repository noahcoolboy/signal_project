package com.alerts;

public class HypotensiveHypoxemiaAlert extends Alert {
    private int systolic;
    private int diastolic;
    private double saturationLevel;

    public HypotensiveHypoxemiaAlert(String patientId, String condition, long timestamp, 
            int systolic, int diastolic, double saturationLevel) {
        super(patientId, condition, timestamp);
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.saturationLevel = saturationLevel;
    }

    public int getSystolic() {
        return systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public double getSaturationLevel() {
        return saturationLevel;
    }
}
