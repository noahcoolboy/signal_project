package com.cardio_generator.generators;

import org.junit.jupiter.api.Test;

import com.cardio_generator.outputs.CaptureOutputStrategy;
import com.data_management.PatientRecord;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BloodLevelsDataGeneratorTest {

    @Test
    void testGenerateBloodLevels() {
        int patientId = 1;
        BloodLevelsDataGenerator generator = new BloodLevelsDataGenerator(5);
        CaptureOutputStrategy outputStrategy = new CaptureOutputStrategy();

        generator.generate(patientId, outputStrategy);
        List<PatientRecord> outputs = outputStrategy.getOutputs();

        // Should produce exactly 3 outputs
        assertEquals(3, outputs.size());

        // Check each label exists
        assertTrue(outputs.stream().anyMatch(o -> o.getRecordType().equals("Cholesterol")));
        assertTrue(outputs.stream().anyMatch(o -> o.getRecordType().equals("WhiteBloodCells")));
        assertTrue(outputs.stream().anyMatch(o -> o.getRecordType().equals("RedBloodCells")));

        // Check that values are valid doubles
        for (PatientRecord output : outputs) {
            // No need to parse, as PatientRecord already stores the value as a double
            assertTrue(output.getMeasurementValue() > 0, "Value should be greater than 0");
        }
    }

    @Test
    void testGenerationError() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));
        BloodLevelsDataGenerator generator = new BloodLevelsDataGenerator(1);
        generator.generate(999, null);
        String errorOutput = outContent.toString().trim();
        assertTrue(errorOutput.contains("An error occurred while generating blood levels data for patient 999"),
                "Error message not printed as expected: " + errorOutput);
    }
}
