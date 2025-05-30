package org.wildloop;

import java.io.InputStream;
import java.util.Properties;

/**
 * Manages configuration settings for the simulation.
 * Reads properties from the {@code simulation.properties} configuration file.
 *
 * @see StartApp
 * @see SimulationPanel
 * @see Animal
 * @see Prey
 * @see Predator
 */
public class SimulationConfig {
    /**
     * Object storing loaded configuration parameters
     */
    private static final Properties properties = new Properties();

    // Static initialization block - loads parameters from simulation.properties file
    static {
        try (InputStream input = SimulationConfig.class.getClassLoader().getResourceAsStream("simulation.properties")) {
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load simulation.properties file", e);
        }
    }

    /**
     * Gets the value associated with the given key from the loaded properties
     * and converts it to an integer.
     *
     * @param key key for which the value should be retrieved
     * @return integer value corresponding to the given key
     * @throws IllegalArgumentException if the value for the given key cannot be converted to an integer
     */
    public static int getValue(String key) {
        String value = properties.getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Value '" + value + "' for parameter '" + key + "' is not a valid integer", e);
        }
    }
}