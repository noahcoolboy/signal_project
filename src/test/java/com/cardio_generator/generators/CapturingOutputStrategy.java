package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

import java.util.ArrayList;
import java.util.List;

public class CapturingOutputStrategy implements OutputStrategy {
    public static class Output {
        public final int patientId;
        public final long timestamp;
        public final String label;
        public final String data;

        public Output(int patientId, long timestamp, String label, String data) {
            this.patientId = patientId;
            this.timestamp = timestamp;
            this.label = label;
            this.data = data;
        }
    }

    private final List<Output> outputs = new ArrayList<>();

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        outputs.add(new Output(patientId, timestamp, label, data));
    }

    public List<Output> getOutputs() {
        return outputs;
    }

    public void clear() {
        outputs.clear();
    }
}
