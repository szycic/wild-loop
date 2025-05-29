package org.wildloop;

import javax.swing.*;

/**
 * Główna klasa aplikacji, która inicjalizuje interfejs graficzny.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StartApp();
        });
    }
}