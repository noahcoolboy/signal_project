package com.data_management;

import java.io.IOException;

/**
 * Classes which implement {@link DataReader} allow for reading of data from a
 * specified source and storing it in a {@link DataStorage}.
 */
public interface DataReader {
    /**
     * Reads data from a specified source and stores it in the data storage.
     * 
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    void readData(DataStorage dataStorage) throws IOException;
}
