package com.alerts.decorators;
import com.alerts.Alert;
public class RepeatedAlertDecorator extends AlertDecorator {
    /**
     * The {@code RepeatedAlertDecorator} class is a concrete implementation of the
     * {@link AlertDecorator} class. It adds functionality to track the number of
     * times an alert has been repeated.
     * <p>
     * This class contains a counter that increments each time the alert is repeated.
     * It also provides a method to get the current count of repetitions.
     */
    
    private int repeatCount;

    public RepeatedAlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert);
        this.repeatCount = 0;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void incrementRepeatCount() {
        repeatCount++;
    }
    
}
