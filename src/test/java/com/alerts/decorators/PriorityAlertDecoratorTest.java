package com.alerts.decorators;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.alerts.Alert;

/**
 * Test class for the PriorityAlertDecorator.
 */
public class PriorityAlertDecoratorTest {

    @Test
    void testPriorityAlertDecoratorConstructorAndGetters() {
        String patientId = "12345";
        String condition = "Test Condition";
        long timestamp = System.currentTimeMillis();
        int priorityLevel = 3;
        
        Alert originalAlert = new Alert(patientId, condition, timestamp);
        PriorityAlertDecorator decorator = new PriorityAlertDecorator(originalAlert, priorityLevel);
        
        assertEquals(patientId, decorator.getPatientId(), "Decorator should inherit patient ID from decorated alert");
        assertEquals(condition, decorator.getCondition(), "Decorator should inherit condition from decorated alert");
        assertEquals(timestamp, decorator.getTimestamp(), "Decorator should inherit timestamp from decorated alert");
        assertEquals(priorityLevel, decorator.getPriorityLevel(), "Priority level should match the constructor argument");
        assertSame(originalAlert, decorator.getDecoratedAlert(), "getDecoratedAlert should return the original alert");
    }
    
    @Test
    void testSetPriorityLevel() {
        Alert originalAlert = new Alert("12345", "Test Condition", System.currentTimeMillis());
        int initialPriority = 3;
        int newPriority = 5;
        
        PriorityAlertDecorator decorator = new PriorityAlertDecorator(originalAlert, initialPriority);
        assertEquals(initialPriority, decorator.getPriorityLevel(), "Initial priority level should match constructor argument");
        
        decorator.setPriorityLevel(newPriority);
        assertEquals(newPriority, decorator.getPriorityLevel(), "Priority level should be updated after setPriorityLevel");
    }
}
