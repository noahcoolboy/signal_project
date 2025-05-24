package com.cardio_generator.inputs;

import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.data_management.DataReader;
import com.data_management.DataStorage;

public class WebSocketDataReader extends WebSocketClient implements DataReader {

    private DataStorage dataStorage;
    public WebSocketDataReader(URI serverUri) { super(serverUri); }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        this.dataStorage = dataStorage;
        if (!this.isOpen()) {
            this.connect();
        }
    }

    @Override
    public void disconnect() {
        this.disconnect();
    }

    @Override
    public void onMessage(String message) {
        if(dataStorage != null) {
            Scanner scanner = new Scanner(message);
            scanner.useDelimiter(",");
            try {
                int patientId = scanner.nextInt();
                long timestamp = scanner.nextLong();
                String label = scanner.next();
                String data = scanner.next();
                double dataValue = 0;
                if ("Alert".equals(label)) {
                    dataValue = "triggered".equals(data) ? 1.0 : 0.0;
                } else if("Saturation".equals(label)) {
                    dataValue = Double.parseDouble(data.substring(0, data.indexOf("%")));
                } else {
                    dataValue = Double.parseDouble(data);
                }
                dataStorage.addPatientData(patientId, dataValue, label, timestamp);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing message: " + message);
            } finally {
                scanner.close();
            }
        }
    }

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {}

    @Override
    public void onError(Exception arg0) { }

    @Override
    public void onOpen(ServerHandshake arg0) {}
    
}
