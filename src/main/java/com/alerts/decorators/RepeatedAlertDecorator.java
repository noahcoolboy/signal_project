package com.alerts.decorators;
import com.alerts.Alert;

/**
 * The {@code RepeatedAlertDecorator} class is a concrete implementation of the
 * {@link AlertDecorator} class. It adds functionality to track the number of
 * times an alert has been repeated.
 * <p>
 * This class contains a counter that increments each time the alert is repeated.
 * It also provides a method to get the current count of repetitions.
 */
public class RepeatedAlertDecorator extends AlertDecorator {
    
    private int repeatCount;

    /**
     * Constructs a new RepeatedAlertDecorator with the specified decorated alert.
     * Initializes the repeat count to zero.
     * 
     * @param decoratedAlert the alert to be decorated
     */
    public RepeatedAlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert);
        this.repeatCount = 0;
    }

    /**
     * Gets the current repeat count of this alert.
     * 
     * @return the number of times this alert has been repeated
     */
    public int getRepeatCount() {
        return repeatCount;
    }

    /**
     * Increments the repeat count of this alert by one.
     */
    public void incrementRepeatCount() {
        repeatCount++;
    }
    
}
