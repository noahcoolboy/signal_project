package com.cardio_generator.outputs;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TcpOutputStrategyTest {

    private TcpOutputStrategy server;
    private static final int TEST_PORT = 8081;

    @BeforeEach
    void setUp() {
        server = new TcpOutputStrategy(TEST_PORT);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (server != null) {
            try {
                server.output(0, 0, "SHUTDOWN", "");
                server.stop();
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    @Test
    void testOutputWithClient() throws Exception {
        Executors.newSingleThreadExecutor().submit(() -> {
            try (Socket clientSocket = new Socket("127.0.0.1", TEST_PORT);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()))) {
                String received = in.readLine();
                assertEquals("1,1000,HeartRate,72.5", received);
            } catch (Exception e) {
                fail("Client failed: " + e.getMessage());
            }
        });

        TimeUnit.MILLISECONDS.sleep(500);

        server.output(1, 1000L, "HeartRate", "72.5");

        TimeUnit.MILLISECONDS.sleep(500);
    }

    @Test
    void testOutputWithoutClientDoesNotThrow() {
        // No client connected
        assertDoesNotThrow(() -> server.output(1, 1000L, "HeartRate", "72.5"));
    }

    @Test
    void testServerSocketFailsToBind() {
        Exception exception = assertThrows(BindException.class, () -> {
            // Occupy the port first to cause bind failure
            ServerSocket occupied = new ServerSocket(TEST_PORT);
            try {
                new TcpOutputStrategy(TEST_PORT);
            } finally {
                occupied.close();
            }
        });

        assertNotNull(exception.getMessage());
    }

    @Test
    void testClientOutputStreamFailure() throws Exception {
        // Set serverSocket to a dummy that will throw IOException
        TcpOutputStrategy faultyServer = new TcpOutputStrategy(TEST_PORT + 1);

        Field serverSocketField = TcpOutputStrategy.class.getDeclaredField("serverSocket");
        serverSocketField.setAccessible(true);
        ServerSocket originalSocket = (ServerSocket) serverSocketField.get(faultyServer);
        originalSocket.close(); // force it to fail on accept()

        // Let background thread attempt to accept and fail
        TimeUnit.MILLISECONDS.sleep(500);

        // Still should not crash
        faultyServer.output(2, 2000L, "Temp", "36.7");
    }

    @Test
    void testOutputActuallySendsMessageWhenOutIsNotNull() throws Exception {
        final String[] receivedLine = new String[1];

        // Start a client to connect and read the message
        Executors.newSingleThreadExecutor().submit(() -> {
            try (Socket clientSocket = new Socket("localhost", TEST_PORT);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(clientSocket.getInputStream()))) {

                // Wait for message
                receivedLine[0] = in.readLine();
            } catch (Exception e) {
                fail("Client error: " + e.getMessage());
            }
        });

        // Wait for server to accept connection and initialize PrintWriter
        TimeUnit.MILLISECONDS.sleep(500);

        // Send the message
        server.output(42, 9999L, "O2", "98.6");

        // Wait to ensure message is sent and received
        TimeUnit.MILLISECONDS.sleep(500);

        // Check received message
        assertEquals("42,9999,O2,98.6", receivedLine[0]);
    }

}