package com.alerts.decorators;
import com.alerts.Alert;

/**
 * The {@code PriorityAlertDecorator} class is a concrete implementation of the
 * {@link AlertDecorator} class. It adds functionality to set the priority of an
 * alert.
 * <p>
 * This class contains a priority level that can be set to indicate the
 * importance of the alert. It also provides methods to get and set the priority
 * level.
 */
public class PriorityAlertDecorator extends AlertDecorator {
    private int priorityLevel;

    /**
     * Constructs a new PriorityAlertDecorator with the specified decorated alert and priority level.
     * 
     * @param decoratedAlert the alert to be decorated
     * @param priorityLevel the priority level to assign to the alert
     */
    public PriorityAlertDecorator(Alert decoratedAlert, int priorityLevel) {
        super(decoratedAlert);
        this.priorityLevel = priorityLevel;
    }

    /**
     * Gets the priority level of this alert.
     * 
     * @return the priority level
     */
    public int getPriorityLevel() {
        return priorityLevel;
    }

    /**
     * Sets the priority level of this alert.
     * 
     * @param priorityLevel the new priority level
     */
    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }
    
}
