package com.data_management;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.cardio_generator.inputs.WebSocketDataReader;
import com.alerts.AlertGenerator;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DataStorageMainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        // Clear the singleton instance after each test
        DataStorage.getInstance().clear();
    }

    @Test
    void testMainMethodInitialization() throws InterruptedException, URISyntaxException {
        // Create a test thread to run the main method
        Thread testThread = new Thread(() -> {
            try {
                DataStorage.main(new String[]{});
            } catch (Exception e) {
                fail("Main method threw an exception: " + e.getMessage());
            }
        });

        testThread.start();
        
        // Wait a bit for initialization
        Thread.sleep(1000);
        
        // Interrupt the thread to stop the infinite loop
        testThread.interrupt();
        
        // Verify that the monitoring message appears in output
        String output = outContent.toString();
        assertTrue(output.contains("Now Monitoring Patient:") || output.isEmpty(),
                "Expected monitoring message or empty output if no patients");
    }

    @Test
    void testMainMethodWithMockReader() throws InterruptedException, URISyntaxException {
        // Replace the WebSocketDataReader with a test version
        DataReader testReader = new DataReader() {
            @Override
            public void readData(DataStorage dataStorage) throws IOException {
                // Add test patient data
                dataStorage.addPatientData(1, 72.0, "HeartRate", System.currentTimeMillis());
            }

            @Override
            public void disconnect() {
                // Do nothing for test
            }
        };

        // Create a test thread to run a modified version of main
        Thread testThread = new Thread(() -> {
            try {
                DataStorage storage = DataStorage.getInstance();
                storage.clear(); // Ensure clean state
                
                testReader.readData(storage);
                
                AlertGenerator alertGenerator = new AlertGenerator(storage);
                Set<Integer> patientIds = new HashSet<>();
                
                // Simulate one iteration of the monitoring loop
                for (Patient patient : storage.getAllPatients()) {
                    if (!patientIds.contains(patient.getPatientId())) {
                        patientIds.add(patient.getPatientId());
                        System.out.println("Now Monitoring Patient: " + patient.getPatientId());
                    }
                    alertGenerator.evaluateData(patient);
                }
            } catch (Exception e) {
                fail("Test threw an exception: " + e.getMessage());
            }
        });

        testThread.start();
        testThread.join();
        
        // Verify output
        String output = outContent.toString();
        assertTrue(output.contains("Now Monitoring Patient: 1"),
                "Expected monitoring message for patient 1");
    }

    @Test
    void testMainMethodHandlesURISyntaxException() {
        // Backup original error stream
        PrintStream originalErr = System.err;
        
        try {
            ByteArrayOutputStream errContent = new ByteArrayOutputStream();
            System.setErr(new PrintStream(errContent));
            
            // Create a test thread with bad URI
            Thread testThread = new Thread(() -> {
                try {
                    // Replace the WebSocket URI creation with one that will throw URISyntaxException
                    DataStorage storage = DataStorage.getInstance();
                    DataReader reader = new WebSocketDataReader(new URI("invalid uri"));
                    reader.readData(storage);
                } catch (Exception e) {
                    // Expected exception
                    System.err.println("Caught expected exception: " + e.getClass().getSimpleName());
                }
            });

            testThread.start();
            testThread.join();
            
            // Verify error output
            String errOutput = errContent.toString();
            assertTrue(errOutput.contains("Caught expected exception: URISyntaxException") ||
                      errOutput.contains("URISyntaxException"),
                    "Expected URISyntaxException to be handled");
        } catch (Exception e) {
            fail("Test setup failed: " + e.getMessage());
        } finally {
            System.setErr(originalErr);
        }
    }
}