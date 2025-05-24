package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlertGeneratorTest {

    static class CapturingOutputStrategy implements OutputStrategy {
        static class Output {
            final int patientId;
            final long timestamp;
            final String label;
            final String data;

            Output(int patientId, long timestamp, String label, String data) {
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
    }

    @Test
    void testAlertTriggeringAndResolution() {
        int patientId = 1;
        AlertGenerator generator = new AlertGenerator(5);
        CapturingOutputStrategy outputStrategy = new CapturingOutputStrategy();

        // Simulate many calls to trigger alert
        boolean triggeredAtLeastOnce = false;
        for (int i = 0; i < 100; i++) {
            generator.generate(patientId, outputStrategy);
            for (CapturingOutputStrategy.Output output : outputStrategy.getOutputs()) {
                if (output.patientId == patientId && output.label.equals("Alert") && output.data.equals("triggered")) {
                    triggeredAtLeastOnce = true;
                }
            }
            if (triggeredAtLeastOnce) break;
        }

        assertTrue(triggeredAtLeastOnce, "Expected at least one alert to be triggered over 100 runs");

        // Simulate many runs again to ensure it can resolve
        boolean resolvedAtLeastOnce = false;
        for (int i = 0; i < 100; i++) {
            generator.generate(patientId, outputStrategy);
            for (CapturingOutputStrategy.Output output : outputStrategy.getOutputs()) {
                if (output.patientId == patientId && output.label.equals("Alert") && output.data.equals("resolved")) {
                    resolvedAtLeastOnce = true;
                }
            }
            if (resolvedAtLeastOnce) break;
        }

        assertTrue(resolvedAtLeastOnce, "Expected at least one alert to be resolved over 100 runs");
    }
}
