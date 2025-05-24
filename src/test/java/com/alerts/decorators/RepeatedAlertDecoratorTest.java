package com.alerts.decorators;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.alerts.Alert;

/**
 * Test class for the RepeatedAlertDecorator.
 */
public class RepeatedAlertDecoratorTest {

    @Test
    void testRepeatedAlertDecoratorConstructorAndInitialState() {
        String patientId = "12345";
        String condition = "Test Condition";
        long timestamp = System.currentTimeMillis();
        
        Alert originalAlert = new Alert(patientId, condition, timestamp);
        RepeatedAlertDecorator decorator = new RepeatedAlertDecorator(originalAlert);
        
        assertEquals(patientId, decorator.getPatientId(), "Decorator should inherit patient ID from decorated alert");
        assertEquals(condition, decorator.getCondition(), "Decorator should inherit condition from decorated alert");
        assertEquals(timestamp, decorator.getTimestamp(), "Decorator should inherit timestamp from decorated alert");
        assertEquals(0, decorator.getRepeatCount(), "Initial repeat count should be zero");
        assertSame(originalAlert, decorator.getDecoratedAlert(), "getDecoratedAlert should return the original alert");
    }
    
    @Test
    void testIncrementRepeatCount() {
        Alert originalAlert = new Alert("12345", "Test Condition", System.currentTimeMillis());
        RepeatedAlertDecorator decorator = new RepeatedAlertDecorator(originalAlert);
        
        assertEquals(0, decorator.getRepeatCount(), "Initial repeat count should be zero");
        
        decorator.incrementRepeatCount();
        assertEquals(1, decorator.getRepeatCount(), "Repeat count should be 1 after first increment");
        
        decorator.incrementRepeatCount();
        decorator.incrementRepeatCount();
        assertEquals(3, decorator.getRepeatCount(), "Repeat count should be 3 after three increments");
    }
    
    @Test
    void testMultipleDecorators() {
        Alert originalAlert = new Alert("12345", "Test Condition", System.currentTimeMillis());
        RepeatedAlertDecorator repeatedDecorator = new RepeatedAlertDecorator(originalAlert);
        PriorityAlertDecorator priorityDecorator = new PriorityAlertDecorator(repeatedDecorator, 2);
        
        repeatedDecorator.incrementRepeatCount();
        repeatedDecorator.incrementRepeatCount();
        
        assertEquals(2, repeatedDecorator.getRepeatCount(), "Repeat count should be 2 after two increments");
        assertEquals(2, priorityDecorator.getPriorityLevel(), "Priority level should be 2");
        assertEquals("12345", priorityDecorator.getPatientId(), "Patient ID should be preserved through multiple decorators");
        assertEquals("Test Condition", priorityDecorator.getCondition(), "Condition should be preserved through multiple decorators");
    }
}
