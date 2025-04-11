package com.cardio_generator.outputs;

/**
 * A {@link OutputStrategy} which outputs the data into the user's console.
 * Data is formatted as:
 * Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n
 * where %d is a decimal integer and %s is a string.
 */
public class ConsoleOutputStrategy implements OutputStrategy {

    /**
     * Constructs a {@code ConsoleOutputStrategy}.
     */
    public ConsoleOutputStrategy() {}

    /**
     * Output data for a specific patient to the console.
     * @param patientId the patient which this data belongs to
     * @param timestamp the time at which the data was generated
     * @param label what this data represents
     * @param data the actual data to be output
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        System.out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
    }
}
