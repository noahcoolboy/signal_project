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
    private CountDownLatch connectionLatch;
    private CountDownLatch messageLatch;

    @BeforeEach
    void setUp() throws InterruptedException {
        server = new WebSocketOutputStrategy(TEST_PORT);
        // Allow time for the server to start
        Thread.sleep(2000);
        connectionLatch = new CountDownLatch(1);
        messageLatch = new CountDownLatch(1);
    }
    @AfterEach
    void tearDown() {
        if (server != null) {
            try {
                server.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    @Disabled
    void testOutputWithClient() throws Exception {
        WebSocketClient client = new WebSocketClient(new URI("ws://localhost:" + TEST_PORT)) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                connectionLatch.countDown();
            }

            @Override
            public void onMessage(String message) {
                receivedMessage = message;
                messageLatch.countDown();
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
        
        // Wait for client to connect
        assertTrue(connectionLatch.await(2, TimeUnit.SECONDS), "Client connection timed out");
        
        // Send test message
        server.output(1, 1000L, "HeartRate", "72.5");
        
        // Wait for message to be received
        assertTrue(messageLatch.await(2, TimeUnit.SECONDS), "Message not received");
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