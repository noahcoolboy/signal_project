package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * An interface representing a patient data generator, such as {@link BloodLevelsDataGenerator}.
 * Implementations must provide a method to generate data for a specific patient and output it
 * using a specified strategy.
 */
public interface PatientDataGenerator {
    /**
     * Generates patient data, and outputs it using the specified output strategy.
     * @param patientId the ID of the patient for whom data is being generated
     * @param outputStrategy the strategy used to output the generated data
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
