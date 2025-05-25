package com.cardio_generator.outputs;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
class WebSocketOutputStrategyTest {

    private WebSocketOutputStrategy server;
    private static final int TEST_PORT = 8080;
    private TestWebSocketClient client;
    private CountDownLatch connectionLatch;
    private CountDownLatch messageLatch;
    private AtomicReference<String> receivedMessage;

    @BeforeEach
    void setUp() throws InterruptedException {
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        System.setErr(new PrintStream(new ByteArrayOutputStream()));

        // Start the server
        server = new WebSocketOutputStrategy(TEST_PORT);
        
        // Give the server some time to start up
        TimeUnit.SECONDS.sleep(1);
        
        // Initialize test variables
        connectionLatch = new CountDownLatch(1);
        messageLatch = new CountDownLatch(1);
        receivedMessage = new AtomicReference<>();
    }

    @AfterEach
    void tearDown() {
        // Close the client if it exists
        if (client != null && client.isOpen()) {
            client.close();
        }
        
        // Stop the server
        if (server != null) {
            try {
                server.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Test
    void testOutputWithClient() throws Exception {
        // Create and connect the client
        client = createAndConnectClient();
        
        // Wait for the client to connect with a longer timeout
        assertTrue(connectionLatch.await(10, TimeUnit.SECONDS), "Client failed to connect within timeout");
        
        // Send a test message
        int patientId = 1;
        long timestamp = 1000L;
        String label = "HeartRate";
        String data = "72.5";
        server.output(patientId, timestamp, label, data);
        
        // Wait for the message to be received
        assertTrue(messageLatch.await(5, TimeUnit.SECONDS), "Message not received within timeout");
        
        // Verify the message format
        String expectedMessage = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
        assertEquals(expectedMessage, receivedMessage.get());
    }
    
    @Test
    void testServerStartsCorrectly() throws Exception {
        // Create and connect the client
        client = createAndConnectClient();
        
        // Wait for the client to connect with a longer timeout
        assertTrue(connectionLatch.await(10, TimeUnit.SECONDS), "Client failed to connect within timeout");
        
        // If we get here, the client connected successfully, which means the server started correctly
        assertTrue(client.isOpen(), "Client should be connected to the server");
    }
    
    @Test
    void testMultipleClientsReceiveMessages() throws Exception {
        // Create and connect the first client
        client = createAndConnectClient();
        assertTrue(connectionLatch.await(5, TimeUnit.SECONDS), "First client failed to connect within timeout");
        
        // Create a second client
        CountDownLatch secondConnectionLatch = new CountDownLatch(1);
        CountDownLatch secondMessageLatch = new CountDownLatch(1);
        AtomicReference<String> secondReceivedMessage = new AtomicReference<>();
        
        TestWebSocketClient secondClient = new TestWebSocketClient(
            new URI("ws://localhost:" + TEST_PORT),
            secondConnectionLatch,
            secondMessageLatch,
            secondReceivedMessage
        );
        secondClient.connect();
        
        // Wait for the second client to connect with a longer timeout
        assertTrue(secondConnectionLatch.await(10, TimeUnit.SECONDS), "Second client failed to connect within timeout");
        
        // Send a test message
        int patientId = 2;
        long timestamp = 2000L;
        String label = "BloodPressure";
        String data = "120/80";
        server.output(patientId, timestamp, label, data);
        
        // Wait for both clients to receive the message with a longer timeout
        assertTrue(messageLatch.await(10, TimeUnit.SECONDS), "First client did not receive message within timeout");
        assertTrue(secondMessageLatch.await(10, TimeUnit.SECONDS), "Second client did not receive message within timeout");
        
        // Verify both clients received the same message
        String expectedMessage = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
        assertEquals(expectedMessage, receivedMessage.get());
        assertEquals(expectedMessage, secondReceivedMessage.get());
        
        // Clean up the second client
        secondClient.close();
    }
    
    @Test
    void testServerStopClosesConnections() throws Exception {
        // Create and connect the client
        client = createAndConnectClient();
        assertTrue(connectionLatch.await(10, TimeUnit.SECONDS), "Client failed to connect within timeout");
        
        // Verify the client is connected
        assertTrue(client.isOpen(), "Client should be connected to the server");
        
        // Stop the server
        server.stop();
        
        // Wait a bit for the connection to close
        TimeUnit.SECONDS.sleep(1);
        
        // Verify the client is no longer connected
        assertFalse(client.isOpen(), "Client should be disconnected after server stop");
        
        // Set server to null to prevent tearDown from trying to stop it again
        server = null;
    }
    
    @Test
    void testOutputWithEmptyData() throws Exception {
        // Create and connect the client
        client = createAndConnectClient();
        assertTrue(connectionLatch.await(10, TimeUnit.SECONDS), "Client failed to connect within timeout");
        
        // Test with empty data
        int patientId = 3;
        long timestamp = 3000L;
        String label = "EmptyData";
        String data = "";
        
        server.output(patientId, timestamp, label, data);
        assertTrue(messageLatch.await(10, TimeUnit.SECONDS), "Message not received within timeout");
        
        String expectedMessage = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
        assertEquals(expectedMessage, receivedMessage.get());
    }
    
    @Test
    void testOutputWithSpecialCharacters() throws Exception {
        // Create and connect the client
        client = createAndConnectClient();
        assertTrue(connectionLatch.await(10, TimeUnit.SECONDS), "Client failed to connect within timeout");
        
        // Test with special characters in data
        int patientId = 4;
        long timestamp = 4000L;
        String label = "SpecialChars";
        String data = "!@#$%^&*()_+";
        
        server.output(patientId, timestamp, label, data);
        assertTrue(messageLatch.await(10, TimeUnit.SECONDS), "Message not received within timeout");
        
        String expectedMessage = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
        assertEquals(expectedMessage, receivedMessage.get());
    }
    
    @Test
    void testOutputWithEmptyLabel() throws Exception {
        // Create and connect the client
        client = createAndConnectClient();
        assertTrue(connectionLatch.await(10, TimeUnit.SECONDS), "Client failed to connect within timeout");
        
        // Test with empty label
        int patientId = 5;
        long timestamp = 5000L;
        String label = "";
        String data = "NoLabel";
        
        server.output(patientId, timestamp, label, data);
        assertTrue(messageLatch.await(10, TimeUnit.SECONDS), "Message not received within timeout");
        
        String expectedMessage = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
        assertEquals(expectedMessage, receivedMessage.get());
    }

    @Test
    void testOnError() throws Exception {
        Class<?> simpleWebSocket = server.getClass().getDeclaredClasses()[0];
        Constructor<?> constructor = simpleWebSocket.getDeclaredConstructor(InetSocketAddress.class);
        Object instance = constructor.newInstance(new InetSocketAddress(8080));
        simpleWebSocket.getDeclaredMethod("onError", WebSocket.class, Exception.class)
            .invoke(instance, null, new Exception("Test error"));
    }

    @Test
    void testOnMessage() throws Exception {
        Class<?> simpleWebSocket = server.getClass().getDeclaredClasses()[0];
        Constructor<?> constructor = simpleWebSocket.getDeclaredConstructor(InetSocketAddress.class);
        Object instance = constructor.newInstance(new InetSocketAddress(8080));
        simpleWebSocket.getDeclaredMethod("onMessage", WebSocket.class, String.class)
            .invoke(instance, null, "Message");
    }
    
    private TestWebSocketClient createAndConnectClient() throws URISyntaxException, InterruptedException {
        TestWebSocketClient client = new TestWebSocketClient(
            new URI("ws://localhost:" + TEST_PORT),
            connectionLatch,
            messageLatch,
            receivedMessage
        );
        
        // Set connection timeout
        client.setConnectionLostTimeout(0);
        
        // Connect and wait a bit to allow connection to establish
        client.connect();
        TimeUnit.MILLISECONDS.sleep(500);
        
        return client;
    }
    
    /**
     * A WebSocket client for testing the WebSocketOutputStrategy.
     */
    private static class TestWebSocketClient extends WebSocketClient {
        private final CountDownLatch connectionLatch;
        private final CountDownLatch messageLatch;
        private final AtomicReference<String> receivedMessage;
        
        public TestWebSocketClient(URI serverUri, CountDownLatch connectionLatch, 
                                  CountDownLatch messageLatch, AtomicReference<String> receivedMessage) {
            super(serverUri);
            this.connectionLatch = connectionLatch;
            this.messageLatch = messageLatch;
            this.receivedMessage = receivedMessage;
        }
        
        @Override
        public void onOpen(ServerHandshake handshakedata) {
            connectionLatch.countDown();
        }
        
        @Override
        public void onMessage(String message) {
            receivedMessage.set(message);
            messageLatch.countDown();
        }
        
        @Override
        public void onClose(int code, String reason, boolean remote) {
            // Not used in tests
        }
        
        @Override
        public void onError(Exception ex) {
            ex.printStackTrace();
        }
    }
}
