package com.cardio_generator.outputs;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CaptureOutputStrategyTest {

    private CaptureOutputStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new CaptureOutputStrategy();
    }

    @Test
    void testOutputNormalData() {
        strategy.output(1, 1000L, "HeartRate", "72.5");
        assertEquals(1, strategy.getOutputs().size());
        PatientRecord record = strategy.getOutputs().get(0);
        assertEquals(1, record.getPatientId());
        assertEquals(72.5, record.getMeasurementValue());
        assertEquals("HeartRate", record.getRecordType());
        assertEquals(1000L, record.getTimestamp());
    }

    @Test
    void testOutputAlertTriggered() {
        strategy.output(2, 2000L, "Alert", "triggered");
        assertEquals(1.0, strategy.getOutputs().get(0).getMeasurementValue());
    }

    @Test
    void testOutputAlertResolved() {
        strategy.output(3, 3000L, "Alert", "resolved");
        assertEquals(0.0, strategy.getOutputs().get(0).getMeasurementValue());
    }

    @Test
    void testOutputAlertUnknown() {
        strategy.output(4, 4000L, "Alert", "unknown");
        assertEquals(-1.0, strategy.getOutputs().get(0).getMeasurementValue());
    }

    @Test
    void testOutputSaturation() {
        strategy.output(5, 5000L, "Saturation", "98%");
        assertEquals(98.0, strategy.getOutputs().get(0).getMeasurementValue());
    }
    @Test
    void testOutputSaturationOnFalse() {
        strategy.output(5, 5000L, "Saturation", "98");
        assertEquals(98.0, strategy.getOutputs().get(0).getMeasurementValue());
    }
    @Test
    void testOutputInvalidNumber() {
        strategy.output(6, 6000L, "Invalid", "not_a_number");
        assertEquals(0.0, strategy.getOutputs().get(0).getMeasurementValue());
    }

    @Test
    void testClear() {
        strategy.output(1, 1000L, "HeartRate", "72.5");
        strategy.clear();
        assertTrue(strategy.getOutputs().isEmpty());
    }
}