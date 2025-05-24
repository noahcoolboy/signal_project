package com.alerts.decorators;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.alerts.Alert;

/**
 * Test class for the AlertDecorator abstract class.
 */
public class AlertDecoratorTest {

    /**
     * Concrete implementation of AlertDecorator for testing purposes.
     */
    private static class TestAlertDecorator extends AlertDecorator {
        public TestAlertDecorator(Alert decoratedAlert) {
            super(decoratedAlert);
        }
    }

    @Test
    void testAlertDecoratorConstructorAndGetters() {
        String patientId = "12345";
        String condition = "Test Condition";
        long timestamp = System.currentTimeMillis();
        Alert originalAlert = new Alert(patientId, condition, timestamp);
        
        TestAlertDecorator decorator = new TestAlertDecorator(originalAlert);
        
        assertEquals(patientId, decorator.getPatientId(), "Decorator should inherit patient ID from decorated alert");
        assertEquals(condition, decorator.getCondition(), "Decorator should inherit condition from decorated alert");
        assertEquals(timestamp, decorator.getTimestamp(), "Decorator should inherit timestamp from decorated alert");
        assertSame(originalAlert, decorator.getDecoratedAlert(), "getDecoratedAlert should return the original alert");
    }
}
