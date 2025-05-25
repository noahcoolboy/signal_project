package com.data_management;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataStorageTest {
    private DataStorage storage;

    @BeforeEach
    void setUp() {
        storage = DataStorage.getInstance();
        storage.clear(); // Clear any existing data before each test
    }

    @Test
    void testSingletonInstance() {
        DataStorage instance1 = DataStorage.getInstance();
        DataStorage instance2 = DataStorage.getInstance();
        assertSame(instance1, instance2, "DataStorage should be a singleton");
    }

    @Test
    void testAddPatientData() {
        storage.addPatientData(1, 72.5, "HeartRate", 1000);
        storage.addPatientData(1, 120.0, "BloodPressure", 2000);

        List<PatientRecord> records = storage.getRecords(1, 0, 3000);
        assertEquals(2, records.size(), "Should have 2 records for patient 1");
    }

    @Test
    void testGetRecordsTimeRange() {
        storage.addPatientData(1, 72.5, "HeartRate", 1000);
        storage.addPatientData(1, 120.0, "BloodPressure", 2000);
        storage.addPatientData(1, 73.0, "HeartRate", 3000);

        List<PatientRecord> records = storage.getRecords(1, 1500, 2500);
        assertEquals(1, records.size(), "Should have 1 record in the time range");
        assertEquals(120.0, records.get(0).getMeasurementValue());
    }

    @Test
    void testGetAllPatients() {
        storage.addPatientData(1, 72.5, "HeartRate", 1000);
        storage.addPatientData(2, 120.0, "BloodPressure", 2000);

        List<Patient> patients = storage.getAllPatients();
        assertEquals(2, patients.size(), "Should have 2 patients");
    }

    @Test
    void testClear() {
        storage.addPatientData(1, 72.5, "HeartRate", 1000);
        storage.clear();
        List<Patient> patients = storage.getAllPatients();
        assertTrue(patients.isEmpty(), "Storage should be empty after clear");
    }

    
}