package org.wildloop;

import javax.swing.*;

/**
 * Główna klasa aplikacji, zawierająca punkt wejścia do programu.
 * Klasa uruchamia i inicjalizuje aplikację poprzez stworzenie
 * głównego okna w wątku event-dispatching (EDT).
 *
 * @see StartApp
 * @see SimulationPanel
 * @see SimulationConfig
 * @see World
 * @see Position
 * @see Direction
 * @see Animal
 * @see Prey
 * @see Predator
 */
public class Main {
    /**
     * Punkt wejścia aplikacji. Tworzy i uruchamia główne okno aplikacji w wątku EDT.
     * 
     * @param args argumenty wiersza poleceń (nie są wykorzystywane)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartApp::new);
    }
}