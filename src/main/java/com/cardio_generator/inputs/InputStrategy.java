package com.cardio_generator.inputs;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

/**
 * The {@code InputStrategy} interface defines a contract for reading data.
 * Implementations of this interface can provide different input mechanisms.
 */
public interface InputStrategy {
    /**
     * Input data via the implementing {@code InputStrategy}
     * @param storage the {@link DataStorage} instance to read data into
     * @return a list of {@link PatientRecord}s
     */
    void read(DataStorage storage);
}
