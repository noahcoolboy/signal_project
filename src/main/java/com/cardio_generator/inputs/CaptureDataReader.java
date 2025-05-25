package com.cardio_generator.inputs;

import com.cardio_generator.outputs.CaptureOutputStrategy;
import com.data_management.DataReader;
import com.data_management.DataStorage;
import com.data_management.PatientRecord;

/**
 * An in-memory implementation of the DataReader interface.
 * This class captures data in memory and provides it to the DataStorage system when requested.
 * It's useful for testing and for scenarios where data is generated programmatically.
 */
public class CaptureDataReader implements DataReader {

    private CaptureOutputStrategy outputStrategy;

    /**
     * Constructs a new CaptureDataReader with the specified CaptureOutputStrategy.
     * 
     * @param outputStrategy the CaptureOutputStrategy to use for capturing outputs
     */
    public CaptureDataReader(CaptureOutputStrategy outputStrategy) {
        this.outputStrategy = outputStrategy;
    }

    /**
     * Reads all captured inputs and adds them to the provided DataStorage.
     * 
     * @param dataStorage the DataStorage instance where the data will be stored
     */
    @Override
    public void readData(DataStorage dataStorage) {
        for (PatientRecord record : outputStrategy.getOutputs()) {
            dataStorage.addPatientData(
                record.getPatientId(),
                record.getMeasurementValue(),
                record.getRecordType(),
                record.getTimestamp()
            );
        }
    }

}
