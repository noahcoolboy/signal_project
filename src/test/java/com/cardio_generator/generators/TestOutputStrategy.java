package com.cardio_generator.generators;
import com.cardio_generator.outputs.OutputStrategy;

public class TestOutputStrategy implements OutputStrategy {
    public int patientId;
    public long timestamp;
    public String label;
    public String data;

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        this.patientId = patientId;
        this.timestamp = timestamp;
        this.label = label;
        this.data = data;
    }
}
