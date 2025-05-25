package com.alerts.strategies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import javax.xml.crypto.Data;

import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.cardio_generator.generators.ECGDataGenerator;
import com.cardio_generator.inputs.CaptureDataReader;
import com.cardio_generator.outputs.CaptureOutputStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;

public class HypotensiveHypoxemiaAlertStrategyTest {
    
    HypotensiveHypoxemiaAlertStrategy strategy = new HypotensiveHypoxemiaAlertStrategy();
    DataStorage dataStorage = DataStorage.getInstance();

    @Test
    public void testHypotensiveHypoxemiaAlertStrategy() {
        dataStorage.addPatientData(1, 0, "ECG", System.currentTimeMillis());
        Patient patient = dataStorage.getAllPatients().get(0);

        dataStorage.addPatientData(1, 80, "SystolicPressure", System.currentTimeMillis());
        dataStorage.addPatientData(1, 50, "DiastolicPressure", System.currentTimeMillis());
        dataStorage.addPatientData(1, 98, "Saturation", System.currentTimeMillis());
        Alert alert3 = strategy.checkAlert(patient);
        assertNull(alert3, "HypotensiveHypoxemiaAlertStrategy should not produce an alert when conditions are not met");

        dataStorage.addPatientData(1, 90, "Saturation", System.currentTimeMillis());
        Alert alert4 = strategy.checkAlert(patient);
        assertNotNull(alert4, "HypotensiveHypoxemiaAlertStrategy should produce an alert when both hypotension and hypoxemia are detected");
        assertEquals("Hypotensive Hypoxemia detected", alert4.getCondition(), "Alert condition should match");
        assertEquals("1", alert4.getPatientId(), "Alert should be associated with patient 1");
    }

    @Test
    public void testHypotensiveHypoxemiaAlertStrategyMissingData() {
        dataStorage.addPatientData(1, 0, "ECG", System.currentTimeMillis());
        assertNull(strategy.checkAlert(dataStorage.getAllPatients().get(0)), 
            "HypotensiveHypoxemiaAlertStrategy should not produce an alert when SystolicPressure is missing");
        
        dataStorage.addPatientData(1, 80, "SystolicPressure", System.currentTimeMillis());
        assertNull(strategy.checkAlert(dataStorage.getAllPatients().get(0)), 
            "HypotensiveHypoxemiaAlertStrategy should not produce an alert when diastolic pressure is missing");

        dataStorage.addPatientData(1, 50, "DiastolicPressure", System.currentTimeMillis());
        assertNull(strategy.checkAlert(dataStorage.getAllPatients().get(0)), 
            "HypotensiveHypoxemiaAlertStrategy should not produce an alert when oxygen saturation is missing");
    }

}
