package com.data_management;

import org.junit.jupiter.api.Test;

import com.cardio_generator.inputs.FileDataReader;

public class DataReaderTest {

    @Test
    public void testDisconnect() {
        FileDataReader reader = new FileDataReader("test_directory");
        reader.disconnect();
    }

}
