package org.wildloop;

import javax.swing.*;

/**
 * Main application class containing program entry point.
 * The class launches and initializes the application by creating
 * the main window in the event-dispatching thread (EDT).
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
     * Application entry point. Creates and launches the main application window in the EDT thread.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartApp::new);
    }
}