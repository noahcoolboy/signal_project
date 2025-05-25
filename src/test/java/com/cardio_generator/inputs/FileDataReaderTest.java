package com.cardio_generator.inputs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

public class FileDataReaderTest {

    @BeforeEach
    public void setUp() {
        DataStorage.getInstance().clear();
    }

    @Test
    public void testReadDataWithValidFiles(@TempDir Path tempDir) throws IOException {
        // Create test files with valid data
        Path validFile1 = tempDir.resolve("valid1.txt");
        Files.write(validFile1, List.of(
            "Patient ID: 1, Timestamp: 1623456789000, Label: HeartRate, Data: 72.5",
            "Patient ID: 2, Timestamp: 1623456790000, Label: BloodPressure, Data: 0"
        ));

        Path validFile2 = tempDir.resolve("valid2.txt");
        Files.write(validFile2, List.of(
            "Patient ID: 3, Timestamp: 1623456791000, Label: Alert, Data: triggered",
            "Patient ID: 4, Timestamp: 1623456792000, Label: Alert, Data: resolved"
        ));

        DataStorage storage = DataStorage.getInstance();
        FileDataReader reader = new FileDataReader(tempDir.toString());
        reader.readData(storage);

        // Verify all records were processed
        assertEquals(4, storage.getAllPatients().size());
        assertEquals(1, storage.getRecords(1, 0, Long.MAX_VALUE).size());
        assertEquals(1, storage.getRecords(2, 0, Long.MAX_VALUE).size());
        assertEquals(1, storage.getRecords(3, 0, Long.MAX_VALUE).size());
        assertEquals(1, storage.getRecords(4, 0, Long.MAX_VALUE).size());
    }

    @Test
    public void testReadDataWithoutSomeData(@TempDir Path tempDir) throws IOException {
        // Create test files with valid data
        Path validFile1 = tempDir.resolve("valid1.txt");
        Files.write(validFile1, List.of(
            "Timestamp: 1623456790000, Label: BloodPressure, Data: 0"
        ));

        Path validFile2 = tempDir.resolve("valid2.txt");
        Files.write(validFile2, List.of(
            "Patient ID: 1, Label: HeartRate, Data: 72.5"
        ));

        Path validFile3 = tempDir.resolve("valid3.txt");
        Files.write(validFile3, List.of(
            "Patient ID: 1, Timestamp: 1623456789000, Data: 72.5"
        ));

        Path validFile4 = tempDir.resolve("valid4.txt");
        Files.write(validFile4, List.of(
            "Patient ID: 1, Timestamp: 1623456789000, Label: HeartRate"
        ));
        DataStorage storage = DataStorage.getInstance();
        FileDataReader reader = new FileDataReader(tempDir.toString());
        reader.readData(storage);

        // Verify all records were processed
        assertTrue(storage.getAllPatients().isEmpty(), "No records should be processed without complete data");
        assertEquals(0, storage.getRecords(1, 0, Long.MAX_VALUE).size(), "No records should be processed for Patient ID 1");
        assertEquals(0, storage.getRecords(2, 0, Long.MAX_VALUE).size(), "No records should be processed for Patient ID 2");
        assertEquals(0, storage.getRecords(3, 0, Long.MAX_VALUE).size(), "No records should be processed for Patient ID 3");
        assertEquals(0, storage.getRecords(4, 0, Long.MAX_VALUE).size(), "No records should be processed for Patient ID 4");
        
    }

    @Test
    public void testReadDataWithMixedFiles(@TempDir Path tempDir) throws IOException {
        // Create test files with mixed valid and invalid data
        Path mixedFile = tempDir.resolve("mixed.txt");
        Files.write(mixedFile, List.of(
            "Patient ID: 1, Timestamp: 1623456789000, Label: HeartRate, Data: 72.5",
            "Invalid line format",
            "Patient ID: 2, Timestamp: 1623456790000, Label: Alert, Data: triggered",
            "Patient ID: 3, Timestamp: not_a_number, Label: BloodPressure, Data: 120/80"
        ));

        DataStorage storage = DataStorage.getInstance();
        FileDataReader reader = new FileDataReader(tempDir.toString());
        reader.readData(storage);

        // Verify only valid records were processed
        assertEquals(2, storage.getAllPatients().size());
        assertEquals(1, storage.getRecords(1, 0, Long.MAX_VALUE).size());
        assertEquals(1, storage.getRecords(2, 0, Long.MAX_VALUE).size());
    }

    @Test
    public void testReadDataWithEmptyDirectory(@TempDir Path tempDir) {
        DataStorage storage = DataStorage.getInstance();
        FileDataReader reader = new FileDataReader(tempDir.toString());
        reader.readData(storage);

        // Verify no records were processed from empty directory
        assertTrue(storage.getAllPatients().isEmpty());
    }

    @Test
    public void testAlertStates(@TempDir Path tempDir) throws IOException {
        // Create test file with all alert states
        Path alertFile = tempDir.resolve("alerts.txt");
        Files.write(alertFile, List.of(
            "Patient ID: 1, Timestamp: 1623456789000, Label: Alert, Data: triggered",
            "Patient ID: 2, Timestamp: 1623456790000, Label: Alert, Data: resolved",
            "Patient ID: 3, Timestamp: 1623456791000, Label: Alert, Data: unknown_state"
        ));

        DataStorage storage = DataStorage.getInstance();
        FileDataReader reader = new FileDataReader(tempDir.toString());
        reader.readData(storage);

        // Verify alert states were processed correctly
        List<PatientRecord> records1 = storage.getRecords(1, 0, Long.MAX_VALUE);
        assertEquals(1, records1.size());
        assertEquals(0.0, records1.get(0).getMeasurementValue(), 0.001);

        List<PatientRecord> records2 = storage.getRecords(2, 0, Long.MAX_VALUE);
        assertEquals(1, records2.size());
        assertEquals(1.0, records2.get(0).getMeasurementValue(), 0.001);

        List<PatientRecord> records3 = storage.getRecords(3, 0, Long.MAX_VALUE);
        assertEquals(1, records3.size());
        assertEquals(-1.0, records3.get(0).getMeasurementValue(), 0.001);
    }

    @Test
    public void testReadDataWithDirectory(@TempDir Path tempDir) throws IOException {
        // Create a valid file
        Path validFile = tempDir.resolve("valid.txt");
        Files.write(validFile, List.of(
            "Patient ID: 2, Timestamp: 1623456790000, Label: BloodPressure, Data: 80"
        ));

        // Create a directory (which cannot be read as a file)
        Path directory = tempDir.resolve("directory");
        Files.createDirectory(directory);

        DataStorage storage = DataStorage.getInstance();
        FileDataReader reader = new FileDataReader(tempDir.toString());
        reader.readData(storage);

        // Verify that:
        // 1. The valid file was processed
        // 2. The directory was skipped (exception caught and handled)
        assertEquals(1, storage.getAllPatients().size());
        assertEquals(1, storage.getRecords(2, 0, Long.MAX_VALUE).size());
    }

    @Test
    public void testFileReadFail() throws IOException {
        File testFile = new File("tmp/test.txt");
        testFile.getParentFile().mkdirs(); // Ensure the directory exists
        testFile.createNewFile();

        try(MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.readAllLines(testFile.toPath()))
                .thenThrow(new IOException("File read error"));

            DataStorage storage = DataStorage.getInstance();
            FileDataReader reader = new FileDataReader("tmp");
            reader.readData(storage);

            // Verify that no records were added due to the read error
            assertTrue(storage.getAllPatients().isEmpty(), "No records should be processed due to file read error");
        }

        // Clean up the test file
        if (testFile.exists()) {
            testFile.delete();
        }
        if (testFile.getParentFile().exists()) {
            testFile.getParentFile().delete();
        }
    }
}