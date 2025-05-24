package com.cardio_generator.generators;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BloodPressureDataGeneratorTest {

    @Test
    void testGenerateBloodPressure() {
        int patientId = 1;
        BloodPressureDataGenerator generator = new BloodPressureDataGenerator(5);
        CapturingOutputStrategy outputStrategy = new CapturingOutputStrategy();

        generator.generate(patientId, outputStrategy);
        List<CapturingOutputStrategy.Output> outputs = outputStrategy.getOutputs();

        // Should produce exactly 2 outputs
        assertEquals(2, outputs.size());

        boolean systolicFound = false, diastolicFound = false;

        for (CapturingOutputStrategy.Output output : outputs) {
            double value = Double.parseDouble(output.data);
            if (output.label.equals("SystolicPressure")) {
                systolicFound = true;
                assertTrue(value >= 90 && value <= 180, "Systolic value out of range: " + value);
            } else if (output.label.equals("DiastolicPressure")) {
                diastolicFound = true;
                assertTrue(value >= 60 && value <= 120, "Diastolic value out of range: " + value);
            } else {
                fail("Unexpected label: " + output.label);
            }
        }

        assertTrue(systolicFound, "SystolicPressure output missing");
        assertTrue(diastolicFound, "DiastolicPressure output missing");
    }
}
