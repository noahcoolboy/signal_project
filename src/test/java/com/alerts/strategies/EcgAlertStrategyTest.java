package com.alerts.strategies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.cardio_generator.generators.ECGDataGenerator;
import com.cardio_generator.inputs.CaptureDataReader;
import com.cardio_generator.outputs.CaptureOutputStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;

public class EcgAlertStrategyTest {
    
    @Test
    public void testEcgAlertStrategy() {
        CaptureOutputStrategy captureOutputStrategy = new CaptureOutputStrategy();
        CaptureDataReader captureDataReader = new CaptureDataReader(captureOutputStrategy);
        ECGDataGenerator ecgDataGenerator = new ECGDataGenerator(1);
        EcgAlertStrategy ecgAlertStrategy = new EcgAlertStrategy();

        for (int i = 0; i < 61; i++) {
            ecgDataGenerator.generate(1, captureOutputStrategy);
        }
        captureDataReader.readData(DataStorage.getInstance());
        Patient patient = DataStorage.getInstance().getAllPatients().get(0);
        assertNull(ecgAlertStrategy.checkAlert(patient), "ECG Strategy should not produce an alert when data is normal");

        patient.addRecord(999, "ECG", System.currentTimeMillis());
        Alert alert = ecgAlertStrategy.checkAlert(patient);
        assertNotNull(alert, "ECG Strategy should produce an alert when data is abnormal");
        assertEquals("ECG abnormality detected", alert.getCondition(), "Alert condition should match");
        assertEquals("1", alert.getPatientId(), "Alert should be associated with patient 1");
    }

}
