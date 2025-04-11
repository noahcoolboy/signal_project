package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates random alerts for patients based on a Poisson distribution.
 * When a patient is in a "pressed" state (meaning an alert is active), there is a
 * 90% chance that the alert will get resolved.
 * On average, there is a 10% chance of an alert being triggered for each patient
 * in a given time period.
 * The generated data has the "Alert" label, with the data being either
 * "triggered" or "resolved".
 */
public class AlertGenerator implements PatientDataGenerator {

    private static final Random randomGenerator = new Random();
    private boolean[] alertStates; // false = resolved, true = pressed

    /**
     * Creates a new data generator for a number of patients
     * @param patientCount the number of patients to generate data for
     */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Generates an alert data point for a given patient, and outputs it using the provided strategy.
     * The generated data includes the alert state (pressed or resolved).
     * @param patientId the ID of the patient for whom to generate data
     * @param outputStrategy the strategy to use for outputting the generated data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
