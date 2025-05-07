package com.alerts.decorators;

import com.alerts.Alert;

public abstract class AlertDecorator extends Alert {
    /**
     * The {@code AlertDecorator} class is an abstract class that extends the
     * {@link Alert} class. It serves as a base class for creating decorators
     * that can add additional functionality to existing alert classes.
     * <p>
     * This class contains a reference to an {@link Alert} object, which is the
     * alert being decorated. Subclasses should implement the specific decoration
     * logic.
     */
    protected Alert decoratedAlert;

    public AlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert.getPatientId(), decoratedAlert.getCondition(), decoratedAlert.getTimestamp());
        this.decoratedAlert = decoratedAlert;
    }

    public Alert getDecoratedAlert() {
        return decoratedAlert;
    }

}
