package com.data_management;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertSame;
class PatientTest {
    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient(1);
    }

    @Test
    void testAddRecord() {
        patient.addRecord(72.5, "HeartRate", 1000);
        patient.addRecord(120.0, "BloodPressure", 2000);

        List<PatientRecord> records = patient.getRecords(0, 3000);
        assertEquals(2, records.size(), "Should have 2 records");
    }

    @Test
    void testGetRecordsTimeRange() {
        patient.addRecord(72.5, "HeartRate", 1000);
        patient.addRecord(120.0, "BloodPressure", 2000);
        patient.addRecord(73.0, "HeartRate", 3000);

        List<PatientRecord> records = patient.getRecords(1500, 2500);
        assertEquals(1, records.size(), "Should have 1 record in the time range");
        assertEquals(120.0, records.get(0).getMeasurementValue());
    }

    @Test
    void testGetRecordsByLabel() {
        patient.addRecord(72.5, "HeartRate", 1000);
        patient.addRecord(120.0, "BloodPressure", 2000);
        patient.addRecord(73.0, "HeartRate", 3000);
        patient.addRecord(74.0, "HeartRate", 4000);

        List<PatientRecord> records = patient.getRecords("HeartRate", 2);
        assertEquals(2, records.size(), "Should return 2 most recent HeartRate records");
        assertEquals(73.0, records.get(0).getMeasurementValue());
        assertEquals(74.0, records.get(1).getMeasurementValue());
    }

    @Test
    void testGetPatientId() {
        assertEquals(1, patient.getPatientId(), "Should return correct patient ID");
    }
}

class PatientRecordTest {
    @Test
    void testPatientRecordCreation() {
        PatientRecord record = new PatientRecord(1, 72.5, "HeartRate", 1000);
        
        assertEquals(1, record.getPatientId());
        assertEquals(72.5, record.getMeasurementValue());
        assertEquals("HeartRate", record.getRecordType());
        assertEquals(1000, record.getTimestamp());
    }
}