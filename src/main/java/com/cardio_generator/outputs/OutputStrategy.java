package com.cardio_generator.outputs;

/**
 * The {@code OutputStrategy} interface defines a contract for outputting data.
 * Implementations of this interface can provide different output mechanisms,
 */
public interface OutputStrategy {
    /**
     * Output data via the implementing {@code OutputStrategy}
     * @param patientId the patient which this data belongs to
     * @param timestamp the time at which the data was generated
     * @param label what this data represents
     * @param data the actual data to be output
     */
    void output(int patientId, long timestamp, String label, String data);
}
