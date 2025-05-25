package com.cardio_generator.inputs;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WebSocketDataReaderTest {

    private WebSocketDataReader reader;
    private DataStorage storage;

    @BeforeEach
    void setUp() throws Exception {
        storage = DataStorage.getInstance();
        storage.clear();
        reader = new WebSocketDataReader(new URI("ws://localhost:8080"));
        reader.readData(storage);
    }
    @Test
    void testOnMessageWithNormalData() {
        String message = "123,1623456789000,HeartRate,75.0";
        reader.onMessage(message);
        
        List<PatientRecord> records = storage.getRecords(123, 0, Long.MAX_VALUE);
        assertEquals(1, records.size());
        assertEquals(75.0, records.get(0).getMeasurementValue(), 0.001);
    }

    @Test
    void testOnMessageWithTriggeredAlert() {
        String message = "456,1623456790000,Alert,triggered";
        reader.onMessage(message);
        
        List<PatientRecord> records = storage.getRecords(456, 0, Long.MAX_VALUE);
        assertEquals(1, records.size());
        assertEquals(1.0, records.get(0).getMeasurementValue(), 0.001);
    }

    @Test
    void testOnMessageWithResolvedAlert() {
        String message = "456,1623456790000,Alert,resolved";
        reader.onMessage(message);
        
        List<PatientRecord> records = storage.getRecords(456, 0, Long.MAX_VALUE);
        assertEquals(1, records.size());
        assertEquals(0.0, records.get(0).getMeasurementValue(), 0.001);
    }

    @Test
    void testOnMessageWithSaturation() {
        String message = "789,1623456791000,Saturation,98%";
        reader.onMessage(message);
        
        List<PatientRecord> records = storage.getRecords(789, 0, Long.MAX_VALUE);
        assertEquals(1, records.size());
        assertEquals(98.0, records.get(0).getMeasurementValue(), 0.001);
    }

    @Test
    void testOnMessageWithInputMismatchException() {
        String message = "a,1623456791000,HeartRate,75.0";
        reader.onMessage(message);
        assertTrue(storage.getAllPatients().isEmpty());
    }

    @Test
    void testOnClose() {
        assertDoesNotThrow(() -> reader.onClose(1000, "Normal closure", true));
    }

    @Test
    void testOnError() {
        assertDoesNotThrow(() -> reader.onError(new Exception("Test error")));
    }

     @Test
    void testOnOpen() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        
        new Thread(() -> {
            reader.onOpen(new SimpleServerHandshake());
            latch.countDown();
        }).start();
        
        assertTrue(latch.await(1, TimeUnit.SECONDS));
    }

    // Helper class for testing onOpen
    private static class SimpleServerHandshake implements ServerHandshake {
        @Override public short getHttpStatus() { return 101; }
        @Override public String getHttpStatusMessage() { return "Switching Protocols"; }
        @Override public String getFieldValue(String name) { return null; }
        @Override public boolean hasFieldValue(String name) { return false; }
        @Override public byte[] getContent() { return null; }
        @Override public Iterator<String> iterateHttpFields() { return null; }
    }
   
    @Test
void testReadDataWhenNotOpen() throws Exception {
    // Create a new reader that isn't connected
    WebSocketDataReader localReader = new WebSocketDataReader(new URI("ws://localhost:8080"));
    
    // Verify connection is not open initially
    assertFalse(localReader.isOpen());
    
    // This should attempt to connect
    assertDoesNotThrow(() -> localReader.readData(storage));
}
    @Test
void testOnMessageWithNullDataStorage() {
    // Create a reader without calling readData() to initialize dataStorage
    WebSocketDataReader localReader = new WebSocketDataReader(URI.create("ws://localhost:8080"));
    
    String message = "123,1623456789000,HeartRate,75.0";
    
    // Should not throw exceptions when dataStorage is null
    assertDoesNotThrow(() -> localReader.onMessage(message));
    
    // Verify no data was stored
    assertTrue(storage.getAllPatients().isEmpty());
}
    @Test
void testDisconnectProperlyClosesConnection() throws Exception {
    // Setup a real WebSocket server for integration test
    SimpleWebSocketServer server = new SimpleWebSocketServer(8888);
    server.start();
    
    // Create a new reader connected to our test server
    WebSocketDataReader testReader = new WebSocketDataReader(new URI("ws://localhost:8888"));
    testReader.connect();
    
    // Wait for connection
    Thread.sleep(500);
    
    // Test disconnect
    assertDoesNotThrow(() -> testReader.disconnect());
    
    // Verify connection is closed
    assertFalse(testReader.isOpen());
    
    server.stop();
}

// Simple WebSocket server for testing
private static class SimpleWebSocketServer extends org.java_websocket.server.WebSocketServer {
    public SimpleWebSocketServer(int port) {
        super(new java.net.InetSocketAddress(port));
    }
    
    @Override public void onOpen(org.java_websocket.WebSocket conn, org.java_websocket.handshake.ClientHandshake handshake) {}
    @Override public void onClose(org.java_websocket.WebSocket conn, int code, String reason, boolean remote) {}
    @Override public void onMessage(org.java_websocket.WebSocket conn, String message) {}
    @Override public void onError(org.java_websocket.WebSocket conn, Exception ex) {}
    @Override public void onStart() {}
}
 // Test when WebSocket is NOT open
    @Test
    public void testConnectWhenNotOpen() throws Exception {
        URI dummyUri = new URI("ws://dummy");
        
        // Create a test-specific subclass
        class TestReader extends WebSocketDataReader {
            public boolean connectCalled = false;

            public TestReader(URI serverUri) { super(serverUri); }

            @Override
            public boolean isOpen() { return false; } // Force state: not open

            @Override
            public void connect() { connectCalled = true; } // Track method call
        }

        TestReader reader = new TestReader(dummyUri);
        reader.readData(storage); // Use a real DataStorage

        assertTrue(reader.connectCalled, "connect() should be called when not open");
    }

    // Test when WebSocket is already open
    @Test
    public void testNoConnectWhenAlreadyOpen() throws Exception {
        URI dummyUri = new URI("ws://dummy");
        
        // Create a test-specific subclass
        class TestReader extends WebSocketDataReader {
            public boolean connectCalled = false;

            public TestReader(URI serverUri) { super(serverUri); }

            @Override
            public boolean isOpen() { return true; } // Force state: already open

            @Override
            public void connect() { connectCalled = true; } // Track method call
        }

        TestReader reader = new TestReader(dummyUri);
        reader.readData(storage); // Use a real DataStorage

        assertFalse(reader.connectCalled, "connect() should NOT be called when already open");
    }
}
