package org.wildloop;

import java.io.InputStream;
import java.util.Properties;

/**
 * Odpowiada za zarządzanie ustawieniami konfiguracyjnymi symulacji.
 * Odczytuje właściwości z pliku konfiguracyjnego {@code simulation.properties}.
 */
public class SimulationConfig {
    /** Obiekt przechowujący załadowane parametry konfiguracyjne */
    private static final Properties properties = new Properties();

    // Statyczny blok inicjalizujący — ładuje parametry z pliku simulation.properties
    static {
        try (InputStream input = SimulationConfig.class.getClassLoader().getResourceAsStream("simulation.properties")) {
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load simulation.properties", e);
        }
    }

    /**
     * Pobiera wartość powiązaną z podanym kluczem z załadowanych właściwości
     * i konwertuje ją na liczbę całkowitą.
     *
     * @param key klucz, dla którego ma zostać pobrana wartość
     * @return wartość całkowita odpowiadająca podanemu kluczowi
     * @throws IllegalArgumentException jeśli wartość dla danego klucza nie może być przekonwertowana na liczbę całkowitą
     */
    public static int getValue(String key) {
        String value = properties.getProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Wartość " + value + " dla klucza " + key + " nie jest poprawną liczbą całkowitą", e);
        }
    }
}
