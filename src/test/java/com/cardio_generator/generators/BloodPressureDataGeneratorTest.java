package com.cardio_generator.generators;

import org.junit.jupiter.api.Test;

import com.cardio_generator.outputs.CaptureOutputStrategy;
import com.data_management.PatientRecord;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BloodPressureDataGeneratorTest {

    @Test
    void testGenerateBloodPressure() {
        int patientId = 1;
        BloodPressureDataGenerator generator = new BloodPressureDataGenerator(5);
        CaptureOutputStrategy outputStrategy = new CaptureOutputStrategy();

        generator.generate(patientId, outputStrategy);
        List<PatientRecord> outputs = outputStrategy.getOutputs();

        // Should produce exactly 2 outputs
        assertEquals(2, outputs.size());

        boolean systolicFound = false, diastolicFound = false;

        for (PatientRecord output : outputs) {
            double value = output.getMeasurementValue();
            String recordType = output.getRecordType();
            
            if (recordType.equals("SystolicPressure")) {
                systolicFound = true;
                assertTrue(value >= 90 && value <= 180, "Systolic value out of range: " + value);
            } else if (recordType.equals("DiastolicPressure")) {
                diastolicFound = true;
                assertTrue(value >= 60 && value <= 120, "Diastolic value out of range: " + value);
            } else {
                fail("Unexpected label: " + recordType);
            }
        }

        assertTrue(systolicFound, "SystolicPressure output missing");
        assertTrue(diastolicFound, "DiastolicPressure output missing");
    }

    @Test
    void testGenerationError() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));
        BloodPressureDataGenerator generator = new BloodPressureDataGenerator(1);
        generator.generate(999, null);
        String errorOutput = outContent.toString().trim();
        assertTrue(errorOutput.contains("An error occurred while generating blood pressure data for patient 999"),
                "Error message not printed as expected: " + errorOutput);
    }

}
