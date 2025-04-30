package com.alerts;

public class EcgAlert extends Alert {
    private double heartRate;

    public EcgAlert(String patientId, String condition, long timestamp, double heartRate) {
        super(patientId, condition, timestamp);
        this.heartRate = heartRate;
    }

    public double getHeartRate() {
        return heartRate;
    }
}
