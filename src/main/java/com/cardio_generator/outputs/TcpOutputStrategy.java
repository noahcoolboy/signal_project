package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * An {@link OutputStrategy} which outputs the data to connected TCP clients.
 * This strategy listens for incoming TCP connections on a specified port.
 * Data is formatted as comma-separated values (without spacing):
 * PatientID,Timestamp,Label,Data
 */
public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    /**
     * Constructs a {@code TcpOutputStrategy} listening on a given port.
     * @param port the port which will be listening for connection.
     * @throws IOException if there is an error creating the server socket or accepting client connections
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Output data to connected TCP clients.
     * @param patientId the patient which this data belongs to
     * @param timestamp the time at which the data was generated
     * @param label what this data represents
     * @param data the actual data to be output
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
