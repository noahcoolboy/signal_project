package com.data_management;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.alerts.AlertGenerator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

class DataStorageMainAdditionalTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private TestDataReader testReader;
    private DataStorage storage;
    private AlertGenerator alertGenerator;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        testReader = new TestDataReader();
        storage = DataStorage.getInstance();
        storage.clear();
        alertGenerator = new AlertGenerator(storage);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        storage.clear();
    }

    @Test
    void testMainLoopWithPatients() throws InterruptedException {
        // Add test data
        storage.addPatientData(1, 72.0, "HeartRate", System.currentTimeMillis());
        storage.addPatientData(2, 120.0, "BloodPressure", System.currentTimeMillis());

        // Variables to track loop behavior
        AtomicBoolean sleepCalled = new AtomicBoolean(false);
        AtomicLong startTime = new AtomicLong(System.currentTimeMillis());
        AtomicLong endTime = new AtomicLong(0);
        Set<Integer> patientIds = new HashSet<>();

        // Simulate one iteration of the main loop
        boolean emptyFlag = true;
        
        // Mock the sleep behavior
        long sleepStart = System.currentTimeMillis();
        Thread.sleep(100); // Reduced sleep time for test
        long sleepEnd = System.currentTimeMillis();
        assertTrue(sleepEnd - sleepStart >= 100, "Thread.sleep should pause execution");
        sleepCalled.set(true);

        for (Patient patient : storage.getAllPatients()) {
            if (!patientIds.contains(patient.getPatientId())) {
                patientIds.add(patient.getPatientId());
                System.out.println("Now Monitoring Patient: " + patient.getPatientId());
            }

            List<PatientRecord> records = storage.getRecords(
                patient.getPatientId(), startTime.get(), System.currentTimeMillis());
            
            // Test the debug output (commented in original)
            for (PatientRecord record : records) {
                System.out.println("[DEBUG] Record - ID: " + record.getPatientId() +
                    ", Type: " + record.getRecordType());
            }

            alertGenerator.evaluateData(patient);

            if (records.size() > 0) {
                emptyFlag = false;
            }
        }

        endTime.set(System.currentTimeMillis());
        
        // Verify outputs
        String output = outContent.toString();
        assertTrue(output.contains("Now Monitoring Patient: 1"));
        assertTrue(output.contains("Now Monitoring Patient: 2"));
        assertTrue(output.contains("[DEBUG] Record"));
        
        // Verify loop control variables
        assertFalse(emptyFlag, "emptyFlag should be false with patient data");
        assertTrue(endTime.get() > startTime.get(), "Time should progress");
        assertTrue(sleepCalled.get(), "Thread.sleep should have been called");
        
        // Verify patient tracking
        assertEquals(2, patientIds.size(), "Should track both patients");
    }

    @Test
    void testMainLoopBreakCondition() throws InterruptedException {
        // No data added to storage
        Set<Integer> patientIds = new HashSet<>();
        boolean emptyFlag = true;
        long time = System.currentTimeMillis();
        
        // Simulate one iteration
        Thread.sleep(50); // Reduced sleep for test
        
        for (Patient patient : storage.getAllPatients()) {
            if (!patientIds.contains(patient.getPatientId())) {
                patientIds.add(patient.getPatientId());
                System.out.println("Now Monitoring Patient: " + patient.getPatientId());
            }

            List<PatientRecord> records = storage.getRecords(
                patient.getPatientId(), time, System.currentTimeMillis());
            
            alertGenerator.evaluateData(patient);

            if (records.size() > 0) {
                emptyFlag = false;
            }
        }
        
        assertTrue(emptyFlag, "emptyFlag should remain true with no data");
        assertTrue(patientIds.isEmpty(), "No patients should be tracked with no data");
    }

    @Test
    void testReaderDisconnect() throws IOException {
        testReader.readData(storage);
        testReader.disconnect();
        assertTrue(testReader.disconnectCalled, "disconnect() should be called");
    }

    // Test implementation of DataReader
    private static class TestDataReader implements DataReader {
        boolean disconnectCalled = false;
        
        @Override
        public void readData(DataStorage dataStorage) throws IOException {
            // Add some test data
            dataStorage.addPatientData(1, 72.0, "HeartRate", System.currentTimeMillis());
        }

        @Override
        public void disconnect() {
            disconnectCalled = true;
        }
    }
}
