package com.cardio_generator.generators;

import org.junit.jupiter.api.Test;

import com.cardio_generator.outputs.CaptureOutputStrategy;
import com.data_management.PatientRecord;

import static org.junit.jupiter.api.Assertions.*;
class AlertGeneratorTest {

    @Test
    void testAlertTriggeringAndResolution() {
        int patientId = 1;
        AlertGenerator generator = new AlertGenerator(1);
        CaptureOutputStrategy outputStrategy = new CaptureOutputStrategy();

        boolean alertTriggered = false;
        boolean triggeredFlag = false;
        boolean unresolvedFlag = false;
        boolean resolvedFlag = false;
        for (int i = 0; i < 2000; i++) { generator.generate(patientId, outputStrategy); }
        for (PatientRecord output : outputStrategy.getOutputs()) {
            if (output.getPatientId() == patientId) {
                String recordType = output.getRecordType();
                double value = output.getMeasurementValue();
                
                if (recordType.equals("Alert")) {
                    if (value == 1.0) { // triggered
                        alertTriggered = true;
                        triggeredFlag = true;
                    } else if (triggeredFlag && value == 0.0) { // resolved
                        resolvedFlag = true;
                    } else if (triggeredFlag) {
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
