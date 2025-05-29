package org.wildloop;

import java.io.InputStream;
import java.util.Properties;

public class SimulationConfig {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = SimulationConfig.class.getClassLoader().getResourceAsStream("simulation.properties")) {
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load simulation.properties", e);
        }
    }

    public static int getValue(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
}
