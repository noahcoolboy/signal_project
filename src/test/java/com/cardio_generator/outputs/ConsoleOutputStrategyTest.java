package com.cardio_generator.outputs;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleOutputStrategyTest {

    @Test
    void testOutput() {
        // Redirect System.out to capture output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ConsoleOutputStrategy strategy = new ConsoleOutputStrategy();
        strategy.output(1, 1000L, "HeartRate", "72.5");
        
        String expected = "Patient ID: 1, Timestamp: 1000, Label: HeartRate, Data: 72.5" + System.lineSeparator();
        assertEquals(expected, outContent.toString());

        // Reset System.out
        System.setOut(System.out);
    }
}