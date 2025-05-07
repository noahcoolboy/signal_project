package com.alerts.decorators;
import com.alerts.Alert;

public class PriorityAlertDecorator extends AlertDecorator {
    /**
     * The {@code PriorityAlertDecorator} class is a concrete implementation of the
     * {@link AlertDecorator} class. It adds functionality to set the priority of an
     * alert.
     * <p>
     * This class contains a priority level that can be set to indicate the
     * importance of the alert. It also provides methods to get and set the priority
     * level.
     */
    private int priorityLevel;

    public PriorityAlertDecorator(Alert decoratedAlert, int priorityLevel) {
        super(decoratedAlert);
        this.priorityLevel = priorityLevel;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }
    
}
