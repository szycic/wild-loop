package org.wildloop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimulationConfigTest {
    @Test
    void getIntValueReturnsCorrectInt() {
        assertEquals(66, SimulationConfig.getIntValue("test.value")); // Assuming "test.value" is defined in the test config file with a value of 66
    }

    @Test
    void getIntValueThrowsExceptionForInvalidKey() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            SimulationConfig.getIntValue("invalid.key"); // Assuming "invalid.key" is not defined in the test config file
        });
        assertEquals("Invalid configuration key: invalid.key", exception.getMessage()); // Check if the exception message matches the expected output
    }

    @Test
    void getIntValueThrowsExceptionForNonIntegerValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            SimulationConfig.getIntValue("test.string"); // Assuming "test.string" is defined in the test config file with a non-integer value like "abc"
        });
        assertEquals("Value 'abc' for parameter 'test.string' is not a valid integer", exception.getMessage()); // Check if the exception message matches the expected output
    }
}
