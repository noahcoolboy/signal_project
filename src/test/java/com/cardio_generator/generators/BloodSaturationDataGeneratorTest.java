package com.cardio_generator.generators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.cardio_generator.outputs.CaptureOutputStrategy;
import com.data_management.PatientRecord;

public class BloodSaturationDataGeneratorTest {
    
    @Test
    void testGenerateBloodSaturation() {
        int patientId = 1;
        BloodSaturationDataGenerator generator = new BloodSaturationDataGenerator(5);
        CaptureOutputStrategy outputStrategy = new CaptureOutputStrategy();

        generator.generate(patientId, outputStrategy);
        List<PatientRecord> outputs = outputStrategy.getOutputs();

        // Should produce exactly 1 output
        assertEquals(1, outputs.size());

        boolean saturationFound = false;

        for (PatientRecord output : outputs) {
            double value = output.getMeasurementValue();
            String recordType = output.getRecordType();
            
            if (recordType.equals("Saturation")) {
                saturationFound = true;
                assertTrue(value >= 90 && value <= 100, "Blood saturation value out of range: " + value);
            } else {
                fail("Unexpected label: " + recordType);
            }
        }

        assertTrue(saturationFound, "Saturation output missing");
    }

    @Test
    void testGenerationError() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));
        BloodSaturationDataGenerator generator = new BloodSaturationDataGenerator(1);
        generator.generate(999, null);
        String errorOutput = outContent.toString().trim();
        assertTrue(errorOutput.contains("An error occurred while generating blood saturation data for patient 999"),
                "Error message not printed as expected: " + errorOutput);
    }

}
