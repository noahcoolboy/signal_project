package com.cardio_generator.inputs;

import java.io.IOException;
import java.net.URI;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.data_management.DataReader;
import com.data_management.DataStorage;

/**
 * A WebSocket implementation of the DataReader interface.
 * This class connects to a WebSocket server to receive real-time patient data
 * and stores it in the DataStorage system.
 */
public class WebSocketDataReader extends WebSocketClient implements DataReader {

    private DataStorage dataStorage;
    
    /**
     * Constructs a new WebSocketDataReader with the specified server URI.
     * 
     * @param serverUri The URI of the WebSocket server to connect to
     */
    public WebSocketDataReader(URI serverUri) { super(serverUri); }

    /**
     * Initiates reading data from the WebSocket server.
     * This method establishes a connection to the server if not already connected.
     * 
     * @param dataStorage The storage system where the data will be stored
     * @throws IOException If there is an error connecting to the server
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        this.dataStorage = dataStorage;
        if (!this.isOpen()) {
            this.connect();
        }
    }

    /**
     * Disconnects from the WebSocket server.
     */
    @Override
    public void disconnect() {
        this.close();
    }

    

    /**
     * Handles incoming messages from the WebSocket server.
     * Parses the message and adds the data to the storage system.
     * 
     * @param message The message received from the server
     */
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
            } catch (InputMismatchException e) {
                System.err.println("Input mismatch in message: " + message);
            }
            finally {
                scanner.close();
            }
        }
    }

    /**
     * Handles the WebSocket connection closing.
     * 
     * @param code The status code indicating why the connection was closed
     * @param reason A human-readable explanation of why the connection was closed
     * @param remote Whether the connection was closed by the remote endpoint
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {}

    /**
     * Handles errors that occur in the WebSocket connection.
     * 
     * @param ex The exception that describes the error
     */
    @Override
    public void onError(Exception ex) { }

    /**
     * Handles the WebSocket connection opening.
     * 
     * @param handshake The handshake data from the server
     */
    @Override
    public void onOpen(ServerHandshake handshake) {}
    
}
