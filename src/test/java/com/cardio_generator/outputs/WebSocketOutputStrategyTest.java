package com.cardio_generator.outputs;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class WebSocketOutputStrategyTest {

    private WebSocketOutputStrategy server;
    private static final int TEST_PORT = 12346;
    private String receivedMessage;
    private CountDownLatch latch;

    @BeforeEach
    void setUp() {
        server = new WebSocketOutputStrategy(TEST_PORT);
        latch = new CountDownLatch(1);
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            try {
                server.output(0, 0, "SHUTDOWN", "");
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    @Test
    @Disabled
    void testOutputWithClient() throws Exception {
        // Create WebSocket client
        WebSocketClient client = new WebSocketClient(new URI("ws://localhost:" + TEST_PORT)) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("Client connected");
            }

            @Override
            public void onMessage(String message) {
                receivedMessage = message;
                latch.countDown();
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("Client disconnected");
            }

            @Override
            public void onError(Exception ex) {
                fail("Client error: " + ex.getMessage());
            }
        };

        client.connect();
        
        // Wait for connection
        TimeUnit.MILLISECONDS.sleep(500);
        
        // Send test message
        server.output(1, 1000L, "HeartRate", "72.5");
        
        // Wait for message to be received
        assertTrue(latch.await(2, TimeUnit.SECONDS));
        assertEquals("1,1000,HeartRate,72.5", receivedMessage);
        
        client.close();
    }
    // @Test
    // void testOnError() {
    //     // Create a WebSocket client that will trigger an error
    //     WebSocketClient client = new WebSocketClient(URI.create("ws://invalid-url")) {
    //         @Override
    //         public void onOpen(ServerHandshake handshakedata) {}

    //         @Override
    //         public void onMessage(String message) {}

    //         @Override
    //         public void onClose(int code, String reason, boolean remote) {}

    //         @Override
    //         public void onError(Exception ex) {
    //             assertNotNull(ex);
    //             System.out.println("Error handled: " + ex.getMessage());
    //         }
    //     };

    //     // Connect and expect an error
    //     assertThrows(Exception.class, client::connect);
    // }

}