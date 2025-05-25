package com.cardio_generator.generators;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BloodLevelsDataGeneratorTest {

    @Test
    void testGenerateBloodLevels() {
        int patientId = 1;
        BloodLevelsDataGenerator generator = new BloodLevelsDataGenerator(5);
        CapturingOutputStrategy outputStrategy = new CapturingOutputStrategy();

        generator.generate(patientId, outputStrategy);
        List<CapturingOutputStrategy.Output> outputs = outputStrategy.getOutputs();

        // Should produce exactly 3 outputs
        assertEquals(3, outputs.size());

        // Check each label exists
        assertTrue(outputs.stream().anyMatch(o -> o.label.equals("Cholesterol")));
        assertTrue(outputs.stream().anyMatch(o -> o.label.equals("WhiteBloodCells")));
        assertTrue(outputs.stream().anyMatch(o -> o.label.equals("RedBloodCells")));

        // Check that values are parseable as doubles
        for (CapturingOutputStrategy.Output output : outputs) {
            assertDoesNotThrow(() -> Double.parseDouble(output.data), "Data not parseable as double: " + output.data);
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
