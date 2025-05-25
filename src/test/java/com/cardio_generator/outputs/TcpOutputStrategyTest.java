package com.cardio_generator.outputs;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TcpOutputStrategyTest {

    private TcpOutputStrategy server;
    private static final int TEST_PORT = 12345;

    @BeforeEach
    void setUp() {
        server = new TcpOutputStrategy(TEST_PORT);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (server != null) {
            // Need to properly close resources
            try {
                server.output(0, 0, "SHUTDOWN", "");
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    @Test
    void testOutputWithClient() throws Exception {
        // Start a client in a separate thread
        Executors.newSingleThreadExecutor().submit(() -> {
            try (Socket clientSocket = new Socket("localhost", TEST_PORT);
                 BufferedReader in = new BufferedReader(
                     new InputStreamReader(clientSocket.getInputStream()))) {
                
                String received = in.readLine();
                assertEquals("1,1000,HeartRate,72.5", received);
            } catch (Exception e) {
                fail("Client failed: " + e.getMessage());
            }
        });

        // Wait for client to connect
        TimeUnit.MILLISECONDS.sleep(500);
        
        // Send test message
        server.output(1, 1000L, "HeartRate", "72.5");
        
        // Give time for message to be received
        TimeUnit.MILLISECONDS.sleep(500);
    }
//     @Test
//     void testOutputWithNullOut() {
//         // Test with null output
//         Exception exception = assertThrows(NullPointerException.class, () -> {
//             server.output(1, 1000L, "HeartRate", null);
//         });
//         assertEquals("Data cannot be null", exception.getMessage());
//     }
}