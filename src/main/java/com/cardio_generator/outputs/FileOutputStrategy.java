package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A {@link OutputStrategy} which outputs the data into a user-provided directory.
 * Each file will be named after the label of the data it contains.
 * Data is formatted as:
 * Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n
 * where %d is a decimal integer and %s is a string.
 */
public class FileOutputStrategy implements OutputStrategy {

    private String baseDirectory;

    /**
     * A map from label to file path.
     * Useful for getting the file path for a specific type of data.
     */
    public final ConcurrentHashMap<String, String> file_map = new ConcurrentHashMap<>();

    /**
     * Constructs a {@code FileOutputStrategy} with a specified base directory.
     * @param baseDirectory the base directory where the output files will be created.
     */
    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * Output data for a specific patient to a file.
     * @param patientId the patient which this data belongs to
     * @param timestamp the time at which the data was generated
     * @param label what this data represents (this will also be used as the file name)
     * @param data the actual data to be output
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        String FilePath = file_map.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(FilePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + FilePath + ": " + e.getMessage());
        }
    }
}