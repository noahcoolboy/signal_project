package com.cardio_generator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import static com.github.stefanbirkner.systemlambda.SystemLambda.*;

import org.junit.jupiter.api.Test;

public class HealthDataSimulatorTest {

    static class HealthDataSimulatorMock extends HealthDataSimulator {
        public HealthDataSimulatorMock() { super(); }
    }

    @Test
    public void testPrintHelp() throws Exception {
        String output = tapSystemOut(() -> {
            HealthDataSimulatorMock.parseArguments(new String[]{"-h"});
        });
        assertTrue(output.contains("Usage: java HealthDataSimulator"));
    }

    @Test
    public void testPatientCount() throws Exception {
        // Invalid patient count
        String err = tapSystemErrAndOut(() -> {
            HealthDataSimulatorMock.parseArguments(new String[]{"--patient-count", "a"});
        });
        assertTrue(err.contains("Invalid number of patients. Using default value"));

        // Valid patient count
        assertNothingWrittenToSystemOut(() -> {
            HealthDataSimulatorMock.parseArguments(new String[]{"--patient-count", "10"});
        });

        // Invalid argument count
        assertNothingWrittenToSystemOut(() -> {
            HealthDataSimulatorMock.parseArguments(new String[]{"--patient-count"});
        });
    }

    @Test
    public void testOutputStrategy() throws Exception {
        // Invalid argument count
        assertNothingWrittenToSystemOut(() -> {
            HealthDataSimulatorMock.parseArguments(new String[]{"--output"});
        });

        // Valid console output
        assertNothingWrittenToSystemOut(() -> {
            HealthDataSimulatorMock.parseArguments(new String[]{"--output", "console"});
        });

        // Valid file output
        assertNothingWrittenToSystemOut(() -> {
            HealthDataSimulatorMock.parseArguments(new String[]{"--output", "file:tmp"});
        });
        File outputDir = new File("tmp");
        assertTrue(outputDir.exists() && outputDir.isDirectory(), "Output directory should exist: " + outputDir.getAbsolutePath());
        assertNothingWrittenToSystemOut(() -> {
            HealthDataSimulatorMock.parseArguments(new String[]{"--output", "file:tmp"});
        });
        assertTrue(outputDir.exists() && outputDir.isDirectory(), "Output directory should still exist: " + outputDir.getAbsolutePath());
        // Clean up after test
        outputDir.delete();

        // Valid WebSocket output
        String out = tapSystemErrAndOut(() -> {
            HealthDataSimulatorMock.parseArguments(new String[]{"--output", "websocket:8080"});
        });
        assertTrue(out.contains("WebSocket output will be on port: 8080"));

        // Invalid WebSocket port
        out = tapSystemErrAndOut(() -> {
            HealthDataSimulatorMock.parseArguments(new String[]{"--output", "websocket:abc"});
        });
        assertTrue(out.contains("Invalid port for WebSocket output. Please specify a valid port number."));

        // Valid TCP output
        out = tapSystemErrAndOut(() -> {
            HealthDataSimulatorMock.parseArguments(new String[]{"--output", "tcp:9090"});
        });
        assertTrue(out.contains("TCP socket output will be on port: 9090"));

        // Invalid TCP port
        out = tapSystemErrAndOut(() -> {
            HealthDataSimulatorMock.parseArguments(new String[]{"--output", "tcp:xyz"});
        });
        assertTrue(out.contains("Invalid port for TCP output. Please specify a valid port number."));

        // Invalid output type
        out = tapSystemErrAndOut(() -> {
            HealthDataSimulatorMock.parseArguments(new String[]{"--output", "unknown"});
        });
        assertTrue(out.contains("Unknown output type. Using default (console)."));
    }

    @Test
    public void testUnknownOption() throws Exception {
        String out = tapSystemErrAndOut(() -> {
            HealthDataSimulatorMock.parseArguments(new String[]{"--unknown-option"});
        });
        assertTrue(out.contains("Unknown option '--unknown-option'"));
        assertTrue(out.contains("Usage: java HealthDataSimulator"));
    }

    @Test
    public void testScheduleTasksForPatients() throws Exception {
        assertNothingWrittenToSystemOut(() -> {
            HealthDataSimulatorMock.parseArguments(new String[]{"--patient-count", "10", "--output", "quiet"});
            List<Integer> patients = HealthDataSimulatorMock.initializePatientIds(10);
            HealthDataSimulatorMock.scheduleTasksForPatients(patients);
            Thread.sleep(3000); // Wait for tasks to complete
        });
        
    }

}
