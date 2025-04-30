package com.cardio_generator.inputs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

/**
 * A {@link OutputStrategy} which outputs the data into a user-provided directory.
 * Each file will be named after the label of the data it contains.
 * Data is formatted as:
 * Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n
 * where %d is a decimal integer and %s is a string.
 */
public class FileInputStrategy implements InputStrategy {

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
    public FileInputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * Reads data from files in the specified base directory.
     * @return a list of {@link PatientRecord}s read from the files.
     * @throws IOException if there is an error reading the files
     */
    public void read(DataStorage storage) {
        for(File file : new File(baseDirectory).listFiles()) {
            if (file.isFile()) {
                try {
                    List<String> lines = Files.readAllLines(file.toPath());
                    for (String line : lines) {
                        if (line.contains("Patient ID:") && line.contains("Timestamp:") && 
                            line.contains("Label:") && line.contains("Data:")) {
                            try {
                                String[] parts = line.split(", ");
                                int patientId = Integer.parseInt(parts[0].substring(parts[0].indexOf(":") + 2));
                                long timestamp = Long.parseLong(parts[1].substring(parts[1].indexOf(":") + 2));
                                String label = parts[2].substring(parts[2].indexOf(":") + 2);
                                String data = parts[3].substring(parts[3].indexOf(":") + 2);
                                
                                double value = 0;
                                if(label.equals("Alert")) {
                                    if(data.equals("triggered"))
                                        value = 0;
                                    else if(data.equals("resolved"))
                                        value = 1;
                                    else
                                        value = -1; // Unknown state
                                } else {
                                    value = Double.parseDouble(data);
                                }

                                storage.addPatientData(patientId, value, label, timestamp);
                            } catch (Exception e) {
                                //System.err.println("Error parsing line: " + line + " - " + e.getMessage());
                            }
                        }
                    }
                } catch (Exception e) {
                    //System.err.println("Error reading file " + file.getName() + ": " + e.getMessage());
                }
            }
        }
    }
}
