package com.data_management;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileDataReaderTest {
    @TempDir
    Path tempDir;
    private FileDataReader reader;
    private DataStorage storage;
    private File testFile;

    @BeforeEach
    void setUp() {
        storage = DataStorage.getInstance();
        storage.clear();
        testFile = tempDir.resolve("data.csv").toFile();
        reader = new FileDataReader(tempDir.toString());
    }

    @Test
    void testReadDataWithValidFile() throws IOException {
        // Create test data file
        String content = "1,72.5,HeartRate,1000\n" +
                        "2,120.0,BloodPressure,2000\n" +
                        "# This is a comment\n" +
                        "1,73.0,HeartRate,3000\n";
        Files.write(testFile.toPath(), content.getBytes());

        reader.readData(storage);

        List<Patient> patients = storage.getAllPatients();
        assertEquals(2, patients.size(), "Should have 2 patients");

        List<PatientRecord> recordsPatient1 = storage.getRecords(1, 0, 4000);
        assertEquals(2, recordsPatient1.size(), "Patient 1 should have 2 records");
        assertEquals(72.5, recordsPatient1.get(0).getMeasurementValue());
        assertEquals("HeartRate", recordsPatient1.get(0).getRecordType());

        List<PatientRecord> recordsPatient2 = storage.getRecords(2, 0, 4000);
        assertEquals(1, recordsPatient2.size(), "Patient 2 should have 1 record");
    }

    @Test
    void testReadDataSkipsInvalidLines() throws IOException {
        String content = "1,72.5,HeartRate,1000\n" +
                        "invalid,data,here\n" +
                        "2,120.0,BloodPressure,2000\n" +
                        "3,missing,fields\n" +
                        "4,90.0,OxygenSaturation,4000\n";
        Files.write(testFile.toPath(), content.getBytes());

        reader.readData(storage);

        List<Patient> patients = storage.getAllPatients();
        assertEquals(3, patients.size(), "Should have 3 valid patients");
    }

    @Test
    void testReadDataSkipsEmptyAndCommentLines() throws IOException {
        String content = "\n" +
                        "# Comment line\n" +
                        "1,72.5,HeartRate,1000\n" +
                        "  \n" +
                        "# Another comment\n" +
                        "2,120.0,BloodPressure,2000\n";
        Files.write(testFile.toPath(), content.getBytes());

        reader.readData(storage);

        List<Patient> patients = storage.getAllPatients();
        assertEquals(2, patients.size(), "Should have 2 patients from non-empty lines");
    }

    @Test
    void testReadDataWithNumberFormatExceptions() throws IOException {
        String content = "1,notANumber,HeartRate,1000\n" +
                        "notAnId,72.5,HeartRate,2000\n" +
                        "3,90.0,OxygenSaturation,notATimestamp\n" +
                        "4,95.0,OxygenSaturation,4000\n";
        Files.write(testFile.toPath(), content.getBytes());

        reader.readData(storage);

        List<Patient> patients = storage.getAllPatients();
        assertEquals(1, patients.size(), "Should have only 1 valid patient");
        assertEquals(4, patients.get(0).getPatientId());
    }

    @Test
    void testReadDataThrowsIOExceptionWhenFileNotFound() {
        FileDataReader nonExistentReader = new FileDataReader("nonexistent_directory");
        assertThrows(IOException.class, () -> nonExistentReader.readData(storage),
            "Should throw IOException for nonexistent file");
    }

    @Test
    void testReadDataWithDifferentWhitespace() throws IOException {
        String content = "1, 72.5, HeartRate , 1000 \n" +
                        "2,120.0  ,BloodPressure,  2000\n" +
                        "3,  90.0,OxygenSaturation,3000  \n";
        Files.write(testFile.toPath(), content.getBytes());

        reader.readData(storage);

        List<Patient> patients = storage.getAllPatients();
        assertEquals(3, patients.size(), "Should handle whitespace properly");
    }

    @Test
    void testReadDataWithEmptyFile() throws IOException {
        Files.write(testFile.toPath(), "".getBytes());

        reader.readData(storage);

        List<Patient> patients = storage.getAllPatients();
        assertTrue(patients.isEmpty(), "Should handle empty file gracefully");
    }
    @Test
    void testReadDataWithNonExistentDirectory() {
        // Directory does not exist
        FileDataReader reader = new FileDataReader(tempDir.resolve("does_not_exist").toString());
        // The directory does not exist, so an IOException should be thrown
        assertThrows(IOException.class, () -> reader.readData(storage),
            "Should throw IOException if directory does not exist");
    }

    @Test
    void testReadDataWithFileInsteadOfDirectory() throws IOException {
        // Create a file instead of a directory
        File file = tempDir.resolve("not_a_directory.txt").toFile();
        Files.write(file.toPath(), "1,72.5,HeartRate,1000\n".getBytes());
        FileDataReader reader = new FileDataReader(file.getAbsolutePath());
        // The path is a file, not a directory, so an IOException should be thrown
        assertThrows(IOException.class, () -> reader.readData(storage),
            "Should throw IOException if path is a file, not a directory");
    }
}