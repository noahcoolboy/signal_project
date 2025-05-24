package com.data_management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Reads patient data from a CSV file in the specified output directory
 * and stores it into the DataStorage instance.
 * Expected CSV format: patientId,measurementValue,recordType,timestamp
 */
public class FileDataReader implements DataReader {

    private final File inputFile;

    /**
     * Constructs a FileDataReader with the given directory path.
     *
     * @param outputDir the directory where the output file is located
     *                  (expects a file named "data.csv" in this directory)
     */
    public FileDataReader(String outputDir) {
        this.inputFile = new File(outputDir, "data.csv");
    }

    /**
     * Reads and parses the data from the CSV file, storing each record into DataStorage.
     *
     * @param dataStorage the data storage where parsed records will be stored
     * @throws IOException if the file does not exist or an I/O error occurs
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        if (!inputFile.exists() || !inputFile.isFile()) {
            throw new IOException("Expected file not found: " + inputFile.getAbsolutePath());
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip empty or comment lines
                if (line.trim().isEmpty() || line.trim().startsWith("#")) continue;

                // Split by commas
                String[] tokens = line.split(",");
                if (tokens.length != 4) {
                    System.err.println("Invalid line format (skipping): " + line);
                    continue;
                }

                try {
                    int patientId = Integer.parseInt(tokens[0].trim());
                    double measurementValue = Double.parseDouble(tokens[1].trim());
                    String recordType = tokens[2].trim();
                    long timestamp = Long.parseLong(tokens[3].trim());

                    dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid data in line (skipping): " + line);
                }
            }
        }
    }
}

