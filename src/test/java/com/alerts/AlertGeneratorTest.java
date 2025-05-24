package com.alerts;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alerts.strategies.AlertStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;

/**
 * Test class for the AlertGenerator.
 */
public class AlertGeneratorTest {
    
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    
    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }
    
    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }
    
    @Test
    void testEvaluateDataWithNoAlerts() {
        DataStorage storage = DataStorage.getInstance();
        Patient patient = new Patient(12345);
        
        AlertGenerator generator = new AlertGenerator(storage);
        generator.evaluateData(patient);
        
        assertEquals("", outContent.toString().trim());
    }
    
    @Test
    void testEvaluateDataWithAlert() {
        DataStorage storage = DataStorage.getInstance();
        Patient patient = new Patient(12345);
        
        AlertGenerator generator = new AlertGenerator(storage) {
            @Override
            public void evaluateData(Patient patient) {
                Alert alert = new Alert(String.valueOf(patient.getPatientId()), "Test Condition", System.currentTimeMillis());
                try {
                    java.lang.reflect.Method method = AlertGenerator.class.getDeclaredMethod("triggerAlert", Alert.class);
                    method.setAccessible(true);
                    method.invoke(this, alert);
                } catch (Exception e) {
                    fail("Failed to invoke triggerAlert method: " + e.getMessage());
                }
            }
        };
        
        generator.evaluateData(patient);
        
        String output = outContent.toString().trim();
        assertTrue(output.contains("[ALERT]"));
        assertTrue(output.contains("Patient ID: 12345"));
        assertTrue(output.contains("Condition: Test Condition"));
    }
    
    @Test
    void testAlertStrategiesAreInitialized() throws Exception {
        DataStorage storage = DataStorage.getInstance();
        
        AlertGenerator generator = new AlertGenerator(storage);
        
        java.lang.reflect.Field field = AlertGenerator.class.getDeclaredField("alertStrategies");
        field.setAccessible(true);
        List<AlertStrategy> strategies = (List<AlertStrategy>) field.get(generator);
        
        assertNotNull(strategies);
        assertEquals(5, strategies.size(), "There should be 5 alert strategies initialized");
    }
}
