package org.wildloop;

import java.io.*;
import java.util.Objects;
import java.util.Properties;

/**
 * Loads configuration from the {@code simulation.properties} file.
 * <p>
 * A static initialization block is used to load the configuration at class load time.
 * <p>
 * If running from a JAR, it copies the properties file to the working directory if it does not already exist,
 * allowing to modify configuration settings without altering the JAR.
 *
 * @see StartApp
 * @see SimulationPanel
 * @see Animal
 * @see Prey
 * @see Predator
 */
public class SimulationConfig {
    private static final String FILE_NAME = "simulation.properties";
    /** Object storing loaded configuration parameters */
    private static final Properties properties = new Properties();

    // Static initialization block
    static {
        boolean runningFromJar = Objects.requireNonNull(SimulationConfig.class.getResource("SimulationConfig.class")).toString().startsWith("jar:");

        if (runningFromJar) {
            File localFile = new File(FILE_NAME);

            // Check if the local file exists, if not, create it by copying from resources
            if (!localFile.exists()) {
                try (
                        InputStream input = SimulationConfig.class.getClassLoader().getResourceAsStream(FILE_NAME);
                        OutputStream output = new FileOutputStream(localFile)
                ) {
                    if (input == null) {
                        throw new RuntimeException("Resource " + FILE_NAME + " not found");
                    }
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = input.read(buffer)) > 0) {
                        output.write(buffer, 0, len);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to create local copy of " + FILE_NAME, e);
                }
            }

            // Load properties from the local file
            try (InputStream input = new FileInputStream(localFile)) {
                properties.load(input);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load " + FILE_NAME + " file", e);
            }
        } else {
            // Load properties directly from the classpath
            try (InputStream input = SimulationConfig.class.getClassLoader().getResourceAsStream(FILE_NAME)) {
                if (input == null) {
                    throw new RuntimeException("Resource " + FILE_NAME + " not found");
                }
                properties.load(input);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load " + FILE_NAME + " file", e);
            }
        }
    }

    /**
     * Gets the value associated with the given key from the loaded properties
     * and converts it to an integer.
     *
     * @param key key for which the value should be retrieved
     * @return integer value corresponding to the given key
     * @throws IllegalArgumentException if the key does not exist in the properties
     * @throws IllegalArgumentException if the value for the given key cannot be converted to an integer
     */
    public static int getIntValue(String key) {
        String value = properties.getProperty(key);
        
        if (value == null) {
            throw new IllegalArgumentException("Invalid configuration key: " + key);
        }
        
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Value '" + value + "' for parameter '" + key + "' is not a valid integer", e);
        }
    }
}