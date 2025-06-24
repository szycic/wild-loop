package org.wildloop;

import javax.swing.*;
import java.awt.*;

/**
 * Extends {@link JFrame} and serves as the main user interface container
 * for the application. Uses {@link CardLayout} to manage and switch between different
 * interface panels such as main menu, settings and simulation screens.
 * <p>
 * The class provides the following functionality:
 * <ul>
 * <li>Initial configuration of the main application window including size, title and behavior</li>
 * <li>Management of the main card layout, enabling switching between different views</li>
 * <li>Creation of specific interface panels (menu, settings and simulation)</li>
 * <li>Handling user interactions such as launching simulation, saving settings and exiting</li>
 * </ul>
 * <p>
 * The application consists of three main panels:
 * <ul>
 * <li>MenuPanel: Contains options to start simulation or exit the application</li>
 * <li>SettingsPanel: Allows user to configure simulation parameters such as world size,
 *     amount of prey and number of predators</li>
 * <li>SimulationPanel: Displays and manages simulation of a configured virtual environment</li>
 * </ul>
 *
 * @see SimulationPanel
 * @see SimulationConfig
 */
public class StartApp extends JFrame {
    /** Card layout used for switching between different interface panels */
    private final CardLayout cardLayout;
    /** Main container panel containing all application panels */
    private final JPanel mainPanel;
    /** Panel responsible for displaying and managing simulation */
    private final SimulationPanel simulationPanel;
    /** Text field for entering simulation world size */
    private JTextField sizeField;
    /** Text field for entering the initial prey count */
    private JTextField preyField;
    /** Text field for entering the initial predator count */
    private JTextField predatorField;

    /**
     * Creates a new application window and initializes all interface components.
     * Sets basic window parameters such as title, size and behavior,
     * and creates and configures all application panels.
     */
    public StartApp() {
        this.setTitle("Wild-loop"); // set a window title
        this.setSize(800, 600); // set window size to 800x600px
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // close an application when a window closes
        //this.setResizable(false); // disable window resizing
        this.setLocationRelativeTo(null); // center window on screen

        cardLayout = new CardLayout(); // initialize card layout
        mainPanel = new JPanel(cardLayout); // create the main panel with card layout

        JPanel menuPanel = createMenuPanel(); // create a menu panel by calling method
        JPanel settingsPanel = createSettingsPanel(); // create a settings panel by calling method
        simulationPanel = new SimulationPanel(this); // initialize a simulation panel with reference

        mainPanel.add(menuPanel, "Menu"); // add a menu panel to the main panel
        mainPanel.add(settingsPanel, "Settings"); // add a settings panel to the main panel
        mainPanel.add(simulationPanel, "Simulation"); // add a simulation panel to the main panel

        this.add(mainPanel); // add the main panel to window
        this.setVisible(true); // set the window visible
    }

    /**
     * Creates the main menu panel for the application.
     * Panel contains buttons to start simulation and exit application,
     * arranged in a vertical layout.
     *
     * @return created and configured a menu panel
     */
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10)); // create a panel with 3 rows, 1 column grid layout and 10px gaps
        panel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100)); // set internal margins (top, left, bottom, right)

        JButton startButton = new JButton("Start Simulation"); // create a simulation start button
        JButton exitButton = new JButton("Exit"); // create the exit button

        startButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "Settings"); // switch to a settings panel when button pressed
        });
        exitButton.addActionListener(e -> System.exit(0)); // exit application when button pressed

        // add all buttons to the panel
        panel.add(startButton);
        panel.add(exitButton);

        // return created panel
        return panel;
    }

    /**
     * Creates a simulation configuration parameters panel.
     * Panel contains fields for entering world size, prey and predator counts,
     * and buttons to save settings and return to a menu.
     *
     * @return created and configured settings panel
     */
    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10)); // create panel with 5x2 grid layout and 10px gaps
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // set internal margins

        JLabel sizeLabel = new JLabel("World size:"); // label for world size
        sizeField = new JTextField(Integer.toString(SimulationConfig.getValue("default.world.size"))); // set default world size value

        JLabel preyLabel = new JLabel("Prey count:"); // label for prey count
        preyField = new JTextField(Integer.toString(SimulationConfig.getValue("default.prey.count"))); // set the default prey count

        JLabel predatorLabel = new JLabel("Predator count:"); // label for predator count
        predatorField = new JTextField(Integer.toString(SimulationConfig.getValue("default.predator.count"))); // set the default predator count

        JButton backButton = new JButton("Back"); // label for return to menu
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu")); // switch to the main panel when back button pressed

        JButton saveButton = new JButton("Save settings and start simulation"); // button for saving settings
        saveButton.addActionListener(e -> {
            int size = Integer.parseInt(sizeField.getText()); // get entered world size
            int preyCount = Integer.parseInt(preyField.getText()); // get prey count
            int predatorCount = Integer.parseInt(predatorField.getText()); // get predator count
            simulationPanel.setSimulationParameters(size, preyCount, predatorCount); // set retrieved values in simulation
            // JOptionPane.showMessageDialog(this, "Settings saved!"); // show settings saved message
            cardLayout.show(mainPanel, "Simulation");
            simulationPanel.startSimulation(); // start simulation
        });

        // add everything to the panel
        panel.add(sizeLabel);
        panel.add(sizeField);
        panel.add(preyLabel);
        panel.add(preyField);
        panel.add(predatorLabel);
        panel.add(predatorField);
        panel.add(saveButton);
        panel.add(backButton);

        // return created panel
        return panel;
    }

    /**
     * Switches interface back to the main menu and reset the simulation state.
     * This method is called when simulation ends or the user wants to
     * return to the main menu.
     */
    public void showMenu() {
        simulationPanel.resetSimulation(); // reset simulation data to zero
        cardLayout.show(mainPanel, "Menu"); // switch card view to the main menu
    }
}