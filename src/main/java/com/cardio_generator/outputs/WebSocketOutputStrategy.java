package com.cardio_generator.outputs;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

/**
 * An {@link OutputStrategy} which outputs the data to connected WebSocket clients.
 * This strategy creates a WebSocket server, and sends generated data to connected clients.
 * Data is formatted as comma-separated values (without spacing):
 * PatientID,Timestamp,Label,Data
 * 
 * Status messages are also logged:
 * - "New Connection" - When a websocket client connects to the server
 * - "Closed connection" - When a client terminates its connection with the server
 * - "Server started successfully" - When the server is started and ready to accept connections
 */
public class WebSocketOutputStrategy implements OutputStrategy {

    private WebSocketServer server;

    /**
     * Constructs a {@code WebSocketOutputStrategy} listening on a given port.
     * Prints a status message when the server is created and will be started.
     * @param port the port which will be listening for connection.
     * @throws IllegalStateException if the server fails to start
     */
    public WebSocketOutputStrategy(int port) {
        server = new SimpleWebSocketServer(new InetSocketAddress(port));
        System.out.println("WebSocket server created on port: " + port + ", listening for connections...");
        server.start();
    }

    /**
     * Output data to connected Websocket clients.
     * @param patientId the patient which this data belongs to
     * @param timestamp the time at which the data was generated
     * @param label what this data represents
     * @param data the actual data to be output
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
        // Broadcast the message to all connected clients
        for (WebSocket conn : server.getConnections()) {
            conn.send(message);
        }
    }

    private static class SimpleWebSocketServer extends WebSocketServer {

        public SimpleWebSocketServer(InetSocketAddress address) {
            super(address);
        }

        @Override
        public void onOpen(WebSocket conn, org.java_websocket.handshake.ClientHandshake handshake) {
            System.out.println("New connection: " + conn.getRemoteSocketAddress());
        }

        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
        }

        @Override
        public void onMessage(WebSocket conn, String message) {
            // Not used in this context
        }

        @Override
        public void onError(WebSocket conn, Exception ex) {
            ex.printStackTrace();
        }

        @Override
        public void onStart() {
            System.out.println("Server started successfully");
        }
    }
}
