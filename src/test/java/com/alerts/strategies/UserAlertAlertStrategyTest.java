package com.alerts.strategies;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.data_management.DataStorage;

public class UserAlertAlertStrategyTest {

    DataStorage dataStorage = DataStorage.getInstance();
    UserAlertAlertStrategy userAlertStrategy = new UserAlertAlertStrategy();
    
    @Test
    public void testUserAlertAlertStrategy() {
        dataStorage.clear();
        dataStorage.addPatientData(1, 0, "ECG", System.currentTimeMillis());
        dataStorage.addPatientData(1, 1, "Misc", System.currentTimeMillis());
        dataStorage.addPatientData(1, 1, "Alert", System.currentTimeMillis());
        dataStorage.addPatientData(1, 0, "Alert", System.currentTimeMillis());
        Alert alert = userAlertStrategy.checkAlert(dataStorage.getAllPatients().get(0));
        assertNotNull(alert, "UserAlertAlertStrategy should produce an alert when a user alert is triggered");

        dataStorage.addPatientData(1, 1, "Alert", System.currentTimeMillis());
        alert = userAlertStrategy.checkAlert(dataStorage.getAllPatients().get(0));
        assertNull(alert, "UserAlertAlertStrategy should not produce an alert when an alert is resolved");
    }

}
