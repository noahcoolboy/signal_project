package com.cardio_generator.outputs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.server.WebSocketServer;

public class WebSocketOutputStrategyTest {

    private WebSocketOutputStrategy strategy;
    private int port;
    private PrintStream originalOut;
    private WebSocketServer server; // Accessed via reflection

    @BeforeEach
    void setUp() throws IOException, InterruptedException, NoSuchFieldException, IllegalAccessException {
        // Find a free port
        ServerSocket socket = new ServerSocket(0);
        port = socket.getLocalPort();
        socket.close();

        // Redirect System.out to capture logs
        originalOut = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        strategy = new WebSocketOutputStrategy(port);

        // Access the private 'server' field via reflection
        Field serverField = WebSocketOutputStrategy.class.getDeclaredField("server");
        serverField.setAccessible(true);
        server = (WebSocketServer) serverField.get(strategy);

        // Wait for server to start (check if not closed)
        // while (server.isClosed()) {
        //     Thread.sleep(100);
        // }
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        server.stop();
        System.setOut(originalOut);
    }

    // @Test
    // void testServerStart() {
    //     assertFalse(server.isClosed()); // Server should not be closed if started successfully
    // }

    @Test
    void testClientConnection() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        WebSocketClient client = new WebSocketClient(new URI("ws://localhost:" + port)) {
            @Override public void onOpen(ServerHandshake handshakedata) {}
            @Override public void onMessage(String message) {}
            @Override public void onClose(int code, String reason, boolean remote) {}
            @Override public void onError(Exception ex) {}
        };

        client.connectBlocking();
        Thread.sleep(100); // Allow time for log
        assertTrue(outContent.toString().contains("New connection"));
        client.close();
    }

    @Test
    void testOutputSendsMessage() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        String[] receivedMessage = { null };

        WebSocketClient client = new WebSocketClient(new URI("ws://localhost:" + port)) {
            @Override public void onOpen(ServerHandshake handshakedata) {}
            @Override public void onMessage(String message) {
                receivedMessage[0] = message;
                latch.countDown();
            }
            @Override public void onClose(int code, String reason, boolean remote) {}
            @Override public void onError(Exception ex) {}
        };

        client.connectBlocking();
        strategy.output(1, 12345L, "HR", "85");
        assertTrue(latch.await(2, TimeUnit.SECONDS), "Message not received within timeout");
        assertEquals("1,12345,HR,85", receivedMessage[0]);
        client.close();
    }

    @Test
    void testClientDisconnect() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        WebSocketClient client = new WebSocketClient(new URI("ws://localhost:" + port)) {
            @Override public void onOpen(ServerHandshake handshakedata) {}
            @Override public void onMessage(String message) {}
            @Override public void onClose(int code, String reason, boolean remote) {}
            @Override public void onError(Exception ex) {}
        };

        client.connectBlocking();
        client.closeBlocking();
        Thread.sleep(100); // Allow time for log
        assertTrue(outContent.toString().contains("Closed connection"));
    }

    @Test
    void testOutputWithNoClients() {
        assertDoesNotThrow(() -> strategy.output(1, 12345L, "HR", "85"));
    }
    @Test
    void testOnError() {
        assertDoesNotThrow(() -> server.onError(null, new Exception("Test error")));
    }
    @Test
    void testOnMessage() {
        assertDoesNotThrow(() -> server.onMessage(null, "Test message"));
    }
}