package org.wildloop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * <p>
 * Represents the main graphical user interface for the simulation,
 * consisting of a grid-based world inhabited by predators and prey,
 * and provides controls for managing and displaying the simulation state.
 * </p>
 * <p>
 * This class extends {@link JPanel} and is designed to integrate with the Swing GUI framework.
 * Manages the simulation grid, statistics display, and provides user interaction
 * through controls such as pause and return buttons.
 * </p>
 *
 * @see StartApp
 * @see SimulationConfig
 * @see InfoPanel
 */
public class SimulationPanel extends JPanel {
    /** Default size of a simulation world */
    private static final int DEFAULT_WORLD_SIZE = SimulationConfig.getIntValue("default.world.size");
    /** Default initial prey count */
    private static final int DEFAULT_PREY_COUNT = SimulationConfig.getIntValue("default.prey.count");
    /** Default initial predator count */
    private static final int DEFAULT_PREDATOR_COUNT = SimulationConfig.getIntValue("default.predator.count");

    /** Reference to the main application, used for communication between components */
    private final StartApp startApp;
    /** Simulation world representation containing environment logic and state */
    private World world;
    /** Array of labels representing individual world grid cells */
    private JLabel[][] gridLabels;
    /** Label displaying current simulation statistics */
    private final JLabel statsLabel;
    /** Timer controlling update frequency and simulation speed */
    private Timer timer;
    /** Button used to pause and resume simulation */
    private final JButton pauseButton;
    /** Flag indicating if simulation is currently paused */
    private boolean isPaused = false;
    /** Panel for displaying animal info */
    private final InfoPanel animalInfoPanel;
    /** Currently selected animal */
    private Animal selectedAnimal;

    /**
     * Constructor for a simulation panel in the application. This panel contains
     * a grid layout representing the simulation area, statistics display, and
     * control buttons for pausing simulation and returning to the main menu.
     *
     * @param startApp main application instance used for navigation back to
     *                 the main menu and simulation control.
     */
    public SimulationPanel(StartApp startApp) {
        this.startApp = startApp; // assign passed reference to the class field
        setLayout(new BorderLayout()); // main application layout (borderlayout - zones division)

        JPanel gridPanel = new JPanel(new GridLayout(20, 20, 1, 1)); // 20x20 grid with 1px spacing
        gridLabels = new JLabel[20][20]; // initialize 20x20 labels array

        // create each cell
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                gridLabels[x][y] = new JLabel("·", SwingConstants.CENTER); // create label with dot, content is centered
                gridLabels[x][y].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // set gray border around label
                gridLabels[x][y].setOpaque(true); // enable transparency (allows background color selection)
                gridLabels[x][y].setBackground(Color.WHITE); // set white background for the label
                gridPanel.add(gridLabels[x][y]); // add our label to panel at specific position
            }
        }

        animalInfoPanel = new InfoPanel();
        animalInfoPanel.setBorder(BorderFactory.createTitledBorder("Animal Info"));
        animalInfoPanel.setFont(new Font("Arial", Font.BOLD, 24));

        // add mouse listener to grid labels
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                final int finalX = x;
                final int finalY = y;
                gridLabels[x][y].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleAnimalClick(finalX, finalY); // Handle click on the grid cell
                    }
                });
            }
        }

        // STATISTICS PANEL
        statsLabel = new JLabel("Turn: 0 | Predators: 0 | Prey: 0", SwingConstants.CENTER); // initialize statistics label
        statsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        statsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // set an empty border with 10 px margins

        pauseButton = new JButton("Pause"); // create pause button
        pauseButton.setFont(new Font("Arial", Font.BOLD, 24));
        pauseButton.addActionListener(e -> togglePause()); // action listener toggling pause on click

        JButton backButton = new JButton("Back to menu"); // button to return to a menu
        backButton.setFont(new Font("Arial", Font.BOLD, 24));
        backButton.addActionListener(e -> {
            stopSimulation(); // stop simulation
            startApp.showMenu(); // method showing a menu panel
        });

        // BUTTON CONTAINER
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5)); // create a panel for buttons with the right alignment and 10x5px spacing
        buttonPanel.add(pauseButton); // add pause button to panel
        buttonPanel.add(backButton); // add return button to panel

        // BOTTOM PANEL
        JPanel bottomPanel = new JPanel(new BorderLayout()); // create a bottom panel with borderlayout
        bottomPanel.add(statsLabel, BorderLayout.CENTER); // add statistics label to center part of a bottom panel
        bottomPanel.add(buttonPanel, BorderLayout.EAST); // add a button panel to right part of bottom panel
        bottomPanel.add(animalInfoPanel, BorderLayout.WEST);

        add(gridPanel, BorderLayout.CENTER); // add a grid panel to the center part of the main panel
        add(bottomPanel, BorderLayout.SOUTH); // add a bottom panel to bottom part of main panel
    }

    /**
     * <p>
     * Toggles the simulation pause state.
     * </p>
     * <p>
     * If simulation is currently running, the method pauses simulation
     * by stopping timer and changing pause button text to indicate
     * that simulation can be resumed.
     * </p>
     * <p>
     * If simulation is currently paused, the method resumes simulation
     * by starting the timer and updating button text to indicate
     * that simulation can be paused again.
     * </p>
     * <p>
     * Pause state is tracked by {@code isPaused} field.
     * </p>
     */
    private void togglePause() {
        isPaused = !isPaused; // negate current pause state
        if (isPaused) { // check if isPaused == true
            timer.stop(); // stop simulation
            pauseButton.setText("Resume"); // change button text from "Pause" to "Resume"
            Event.log(EventType.SIMULATION_PAUSE, world); // log pause event
        } else { // if not
            timer.start(); // resume simulation
            pauseButton.setText("Pause"); // change button text from "Resume" to "Pause"
            Event.log(EventType.SIMULATION_RESUME, world); // log resume event
        }
    }

    /**
     * <p>
     * Stops simulation by stopping timer and resetting pause state.
     * </p>
     * <p>
     * Method checks if the simulation timer is active. If so, stops the timer
     * to prevent further simulation updates. Additionally, ensures the pause
     * state is set to false, indicating simulation is no longer paused.
     * </p>
     * <p>
     * Method is called during simulation termination or reset process
     * to restore it to the default state.
     * </p>
     */
    private void stopSimulation() {
        if (timer != null) {
            timer.stop(); // if timer exists then stop it
        }
        isPaused = false; // restore flag to default value
        Event.log(EventType.SIMULATION_END, world); // log simulation end event
        LogExporter.closeLog(); // close a log file
    }

    /**
     * Configures simulation parameters by setting simulated world dimensions,
     * initializing grid for graphical interface and populating simulation
     * with prey and predator type animals based on given numbers.
     *
     * @param size          size of a square world (e.g., 10 creates 10x10 grid)
     * @param preyCount     number of prey type animals to generate in simulation
     * @param predatorCount number of predator type animals to generate in simulation
     */
    public void setSimulationParameters(int size, int preyCount, int predatorCount) {
        this.world = new World(size, size); // create new world with given size
        initializeGrid(size); // initialize GUI grid

        // loop creating prey
        for (int i = 0; i < preyCount; i++) {
            Position pos = getRandomEmptyPosition(world); // select a random empty position
            if (pos != null) {
                new Prey(world, pos);
            }
        }

        // loop creating predators
        for (int i = 0; i < predatorCount; i++) {
            Position pos = getRandomEmptyPosition(world); // select a random empty position
            if (pos != null) {
                new Predator(world, pos);
            }
        }
    }

    /**
     * <p>
     * Starts simulation by initializing or resetting the necessary components
     * and launching the main simulation loop.
     * </p>
     * <p>
     * Method checks if a simulation world is initialized. If not,
     * sets default simulation parameters using {@link #setSimulationParameters(int, int, int)}.
     * Ensures simulation is not paused by resetting a pause flag
     * and updates the control button to display "Pause".
     * </p>
     * <p>
     * Main simulation loop is managed by {@link Timer} with
     * 500-millisecond delay. Each timer tick performs the following actions:
     * <ul>
     * <li>Advances simulation state using {@link World#tick()}.</li>
     * <li>Updates GUI grid using {@link #updateGrid()}.</li>
     * <li>Updates simulation statistics using {@link #updateStats()}.</li>
     * </ul>
     * </p>
     * <p>
     * If simulation reaches end condition, such as no remaining animals
     * in a world, simulation is stopped using {@link #stopSimulation()},
     * and user is shown notification.
     * </p>
     */
    public void startSimulation() {
        // check if world exists
        if (world == null) {
            setSimulationParameters(DEFAULT_WORLD_SIZE, DEFAULT_PREY_COUNT, DEFAULT_PREDATOR_COUNT); // set default parameters
        }

        isPaused = false; // restart flag to default value
        pauseButton.setText("Pause"); // set button text to "pause"
        pauseButton.setFont(new Font("Arial", Font.BOLD, 24));

        if (timer != null) {
            timer.stop(); // if any timer is running, then stop it
        }

        // create a new timer with 500 ms delay
        timer = new Timer(500, e -> {
            // if simulation is not stopped
            if (!isPaused) {
                world.tick(); // perform simulation step
                updateGrid(); // update gui
                updateStats(); // update statistics

                // check simulation end condition (if any living creature exists)
                if (world.getAnimals().isEmpty()) {
                    stopSimulation(); // stop simulation
                    JOptionPane.showMessageDialog(this, "Simulation ended!"); // display simulation end message
                }
            }
        });
        timer.start(); // start timer
    }

    /**
     * Initializes simulation grid of a specified size.
     * This method creates and configures grid layout with labels for square grid,
     * updates the graphical interface and resets previous grid components.
     *
     * @param size size of square grid to initialize (e.g., 10 creates 10x10 grid)
     */
    private void initializeGrid(int size) {
        JPanel gridPanel = (JPanel) getComponent(0); // get grid panel
        gridPanel.removeAll(); // clear panel of existing components
        gridPanel.setLayout(new GridLayout(size, size)); // set a new grid layout
        gridLabels = new JLabel[size][size]; // initialize labels array

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                gridLabels[x][y] = new JLabel("·", SwingConstants.CENTER); // create label with dot, content is centered
                gridLabels[x][y].setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // set gray border around label
                gridLabels[x][y].setOpaque(true); // Enable background coloring
                gridLabels[x][y].setBackground(Color.WHITE); // Set default background

                // Add a mouse listener to each label
                final int finalX = x;
                final int finalY = y;
                gridLabels[x][y].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleAnimalClick(finalX, finalY); // Handle click on the grid cell
                    }
                });

                gridPanel.add(gridLabels[x][y]); // add our label to panel at specific position
            }
        }
        gridPanel.revalidate(); // force layout rebuild
        gridPanel.repaint(); // force layout redraw
    }

    /**
     * <p>
     * Updates visual representation of the simulation grid.
     * </p>
     * <p>
     * Method iterates through each cell in the simulation world grid and updates graphical
     * labels corresponding to each cell based on its current contents. Specifically:
     * <ul>
     *     <li>If cell is empty (value {@code null}), sets label text to "·" to indicate empty space</li>
     *     <li>If cell contains instance of {@link Predator}, sets label text to "P"</li>
     *     <li>If cell contains instance of {@link Prey}, sets label text to "O"</li>
     * </ul>
     * </p>
     */
    private void updateGrid() {
        for (int y = 0; y < world.getHeight(); y++) {
            for (int x = 0; x < world.getWidth(); x++) {
                Animal animal = world.getGrid()[x][y]; // get animal from a given cell
                if (animal == null) {
                    gridLabels[x][y].setText("·"); // if empty inserts dot
                } else if (animal instanceof Predator) {
                    gridLabels[x][y].setText("P"); // if predator inserts P
                } else if (animal instanceof Prey) {
                    gridLabels[x][y].setText("O"); // if prey inserts O
                }
                updateCellAppearance(x, y, world.getGrid()[x][y]);
            }
        }

        if (selectedAnimal != null && !world.getAnimals().contains(selectedAnimal)) {
            animalInfoPanel.showAnimalDead();
            selectedAnimal = null;
        }
    }

    /**
     * <p>
     * Updates simulation statistics by counting the number of predators and prey
     * in a simulated world. Method gets a list of animals, distinguishes predators
     * and prey based on their classes and calculates their totals.
     * </p>
     * <p>
     * Then updates statistics display with current turn number, number of
     * predators, prey and total number of animals.
     * </p>
     */
    private void updateStats() {
        int predatorCount = 0; // predator counter
        int preyCount = 0; // prey counter

        for (Animal animal : world.getAnimals()) {
            if (animal instanceof Predator) {
                predatorCount++; // count all predators
            } else if (animal instanceof Prey) {
                preyCount++; // count all prey
            }
        }

        if (selectedAnimal != null && !world.getAnimals().contains(selectedAnimal)) {
            animalInfoPanel.showAnimalDead();
            selectedAnimal = null;
        } else if (selectedAnimal != null) {
            animalInfoPanel.updateInfo();
        }

        statsLabel.setText(String.format("Turn: %d | Predators: %d | Prey: %d | Total: %d", world.getTurn(), predatorCount, preyCount, world.getAnimals().size())); // format statistics text with current data
    }

    /**
     * Generates random empty position in a given world.
     * Method tries to find unoccupied position within world bounds.
     * Makes a maximum of 100 attempts to find an empty cell.
     * If after 100 attempts no empty cell is found,
     * returns {@code null}.
     *
     * @param world world in which to find random empty position
     * @return randomly selected empty position in a world or {@code null} if such
     * position was not found after 100 attempts
     */
    private Position getRandomEmptyPosition(World world) {
        // random loop, maximum 100 attempts
        for (int attempts = 0; attempts < 100; attempts++) {
            int x = (int) (Math.random() * world.getWidth()); // random X position
            int y = (int) (Math.random() * world.getHeight()); // random Y position
            Position pos = new Position(x, y); // assign random data
            if (world.isCellEmpty(pos)) {
                return pos; // if random cell is empty return our position
            }
        }
        return null; // if no empty space found return null
    }

    /**
     * <p>
     * Restores simulation to the initial state by stopping all active processes,
     * clearing existing simulation data and reinitializing view and necessary components.
     * </p>
     * <p>
     * Functionality:
     * <ul>
     * <li>Stops timer if currently running</li>
     * <li>Resets pause state to false</li>
     * <li>Removes all animals from a simulation world and resets turn counter</li>
     * <li>Reinitializes graphical grid representing a simulation area</li>
     * <li>Updates simulation statistics to reflect the reset state</li>
     * <li>Restores pause button label to default state</li>
     * </ul>
     * </p>
     * <p>
     * Preconditions:
     * <ul>
     * <li>'world' object, if not {@code null}, must properly manage its animal list and turn counter</li>
     * <li>Grid graphics and statistics components must allow reinitialization</li>
     * </ul>
     * </p>
     * <p>
     * Postconditions:
     * <ul>
     * <li>Simulation is set to clean state, ready to start a new simulation run</li>
     * </ul>
     * </p>
     */
    public void resetSimulation() {
        if (timer != null && timer.isRunning()) {
            timer.stop(); // if timer is running then stop it
        }

        isPaused = false; // restore pause flag to initial state

        if (world != null) {
            for (Animal animal : new ArrayList<>(world.getAnimals())) {
                world.removeAnimal(animal); // remove all existing animals
            }
            world.reset(); // reset turn counter
        }

        initializeGrid(world != null ? world.getWidth() : 20); // initialize GUI grid
        updateStats(); // update statistics

        if (pauseButton != null) {
            pauseButton.setText("Pause"); // reset pause button
        }

        selectedAnimal = null;
        animalInfoPanel.setSelectedAnimal(null);
    }

    /**
     * Handles click on a grid cell to select/deselect animals.
     * @param x X coordinate of the clicked cell
     * @param y Y coordinate of the clicked cell
     */
    private void handleAnimalClick(int x, int y) {
        // Get the animal at clicked position
        Animal clickedAnimal = world.getGrid()[x][y];

        // Reset all cell appearances first
        if (clickedAnimal != null && clickedAnimal == selectedAnimal) {
            selectedAnimal = null;
            animalInfoPanel.setSelectedAnimal(null);
        } else if (clickedAnimal != null) {
            selectedAnimal = clickedAnimal;
            animalInfoPanel.setSelectedAnimal(clickedAnimal);
        } else {
            selectedAnimal = null;
            animalInfoPanel.setSelectedAnimal(null);
        }

        for (int i = 0; i < world.getWidth(); i++) {
            for (int j = 0; j < world.getHeight(); j++) {
                Animal currentAnimal = world.getGrid()[i][j];
                if (currentAnimal == selectedAnimal) {
                    gridLabels[i][j].setBackground(animalInfoPanel.getHighlightColor());
                } else if (currentAnimal instanceof Predator) {
                    gridLabels[i][j].setBackground(Color.RED);
                } else if (currentAnimal instanceof Prey) {
                    gridLabels[i][j].setBackground(Color.GREEN);
                } else {
                    gridLabels[i][j].setBackground(Color.WHITE);
                }
            }
        }

    }

    /**
     * Updates the appearance of a single grid cell.
     * @param x X coordinate of the cell
     * @param y Y coordinate of the cell
     * @param animal Animal in the cell (null if empty)
     */
    private void updateCellAppearance(int x, int y, Animal animal) {
        JLabel label = gridLabels[x][y];
        if (animal == null) {
            label.setText("·"); // Empty cell
            label.setBackground(Color.WHITE); // Default background
        } else if (animal instanceof Predator) {
            label.setText("P"); // Predator
            label.setBackground(animal == selectedAnimal ? animalInfoPanel.getHighlightColor() : Color.RED);
        } else if (animal instanceof Prey) {
            label.setText("O"); // Prey
            label.setBackground(animal == selectedAnimal ? animalInfoPanel.getHighlightColor() : Color.GREEN);
        }
    }
}