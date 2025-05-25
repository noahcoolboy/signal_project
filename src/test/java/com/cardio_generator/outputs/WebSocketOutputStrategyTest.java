package com.cardio_generator.outputs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class WebSocketOutputStrategyTest {

    private WebSocketOutputStrategy server;
    private static final int TEST_PORT = 12346;
    

    @BeforeEach
    void setUp() {
        server = new WebSocketOutputStrategy(TEST_PORT);
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
    
}