package com.cardio_generator.generators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class AlertGeneratorTest {

    @Test
    void testAlertTriggeringAndResolution() {
        int patientId = 1;
        AlertGenerator generator = new AlertGenerator(1);
        CapturingOutputStrategy outputStrategy = new CapturingOutputStrategy();

        boolean alertTriggered = false;
        boolean triggeredFlag = false;
        boolean unresolvedFlag = false;
        boolean resolvedFlag = false;
        for (int i = 0; i < 2000; i++) { generator.generate(patientId, outputStrategy); }
        for (CapturingOutputStrategy.Output output : outputStrategy.getOutputs()) {
            if (output.patientId == patientId) {
                if (output.label.equals("Alert") && output.data.equals("triggered")) {
                    alertTriggered = true;
                    triggeredFlag = true;
                } else if (triggeredFlag) {
                    if(output.label.equals("Alert") && output.data.equals("resolved")) {
                        resolvedFlag = true;
                    } else {
                        unresolvedFlag = true;
                    }
                }
            }
        }

        assertTrue(alertTriggered, "Expected at least one alert to be triggered");
        assertTrue(resolvedFlag, "Expected at least one alert to be resolved");
        assertFalse(unresolvedFlag, "Expected at least one alert not to be resolved instantly after being triggered");
        assertTrue(triggeredFlag, "Expected at least one alert to be triggered before resolution");
    }
}
