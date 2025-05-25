package com.cardio_generator.outputs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class WebSocketOutputStrategyTest {

     private WebSocketOutputStrategy server;
    private static final int TEST_PORT = 12346;
    

    @BeforeEach
    void setUp() throws InterruptedException {
        server = new WebSocketOutputStrategy(TEST_PORT);
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
    
}