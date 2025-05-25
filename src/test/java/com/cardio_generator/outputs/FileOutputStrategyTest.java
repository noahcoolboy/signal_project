package com.cardio_generator.outputs;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileOutputStrategyTest {

    @TempDir
    Path tempDir;
    private FileOutputStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new FileOutputStrategy(tempDir.toString());
    }

    @Test
    void testOutputCreatesFile() throws IOException {
        strategy.output(1, 1000L, "HeartRate", "72.5");

        Path filePath = Paths.get(tempDir.toString(), "HeartRate.txt");
        assertTrue(Files.exists(filePath));

        String content = Files.readString(filePath);
        assertTrue(content.contains("Patient ID: 1, Timestamp: 1000, Label: HeartRate, Data: 72.5"));
    }

    @Test
    void testOutputAppendsToFile() throws IOException {
        strategy.output(1, 1000L, "HeartRate", "72.5");
        strategy.output(1, 2000L, "HeartRate", "73.0");

        Path filePath = Paths.get(tempDir.toString(), "HeartRate.txt");
        long lineCount = Files.lines(filePath).count();
        assertEquals(2, lineCount);
    }

    @Test
    void testFileMap() {
        strategy.output(1, 1000L, "HeartRate", "72.5");
        assertTrue(strategy.file_map.containsKey("HeartRate"));
        assertEquals(Paths.get(tempDir.toString(), "HeartRate.txt").toString(),
                strategy.file_map.get("HeartRate"));
    }

    @Test
    public void testOutput_WhenDirectoryCreationFails_ShouldHandleError() {
        // Create a path that we know will fail (trying to create a directory where a
        // file exists)
        Path existingFile = tempDir.resolve("existing_file.txt");
        try {
            Files.createFile(existingFile);
        } catch (IOException e) {
            fail("Test setup failed");
        }

        // This should fail because we can't create a directory with the same name as an
        // existing file
        String invalidBaseDirectory = existingFile.toString();
        FileOutputStrategy strategy = new FileOutputStrategy(invalidBaseDirectory);

        // Redirect System.err to capture the error message
        final java.io.ByteArrayOutputStream errContent = new java.io.ByteArrayOutputStream();
        final java.io.PrintStream originalErr = System.err;
        System.setErr(new java.io.PrintStream(errContent));

        try {
            // Try to output data - should trigger the directory creation error
            strategy.output(1, System.currentTimeMillis(), "test", "data");

            // Verify the error message was printed to System.err
            assertTrue(errContent.toString().contains("Error creating base directory"));
        } finally {
            // Restore System.err
            System.setErr(originalErr);
        }
    }

    @Test
    void testOutputWithEmptyLabel() throws IOException {
        strategy.output(1, 1000L, "", "NoLabelData");

        Path filePath = Paths.get(tempDir.toString(), ".txt");
        assertTrue(Files.exists(filePath));

        String content = Files.readString(filePath);
        assertTrue(content.contains("Patient ID: 1, Timestamp: 1000, Label: , Data: NoLabelData"));
    }

    @Test
    void testOutput_WhenFileWriteFails_ShouldHandleError() throws IOException {
        String label = "HeartRate";
        Path fakeFilePath = Paths.get(tempDir.toString(), label + ".txt");

        // Create a directory where the file is expected to be written
        Files.createDirectory(fakeFilePath);

        // Redirect System.err to capture error output
        final java.io.ByteArrayOutputStream errContent = new java.io.ByteArrayOutputStream();
        final java.io.PrintStream originalErr = System.err;
        System.setErr(new java.io.PrintStream(errContent));

        try {
            // Attempt to write to a directory path as if it were a file
            strategy.output(1, 1000L, label, "72.5");

            // Verify the error message was printed
            assertTrue(errContent.toString().contains("Error writing to file"));
        } finally {
            // Restore original System.err
            System.setErr(originalErr);
        }
    }

}