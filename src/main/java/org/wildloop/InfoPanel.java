package org.wildloop;

import javax.swing.*;
import java.awt.*;

/**
 * Displays information about the currently selected animal in the simulation.
 * It shows details such as type, energy, age, and allows highlighting of the selected animal.
 * The panel updates dynamically based on user interactions with the simulation.
 *
 * @see SimulationPanel
 */
public class InfoPanel extends JPanel {
    /** Label to display animal information */
    private final JLabel infoLabel;
    /** Currently selected animal */
    private Animal selectedAnimal;
    /** Color to highlight selected animal */
    private final Color highlightColor = Color.YELLOW;

    /**
     * Constructor for Animals information panel.
     * Initializes the panel with default settings.
     */
    public InfoPanel() {
        setLayout(new BorderLayout()); // using borderlayout for the panel
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        infoLabel = new JLabel("No animal selected"); // initializing info label
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER); // set text alignment to center
        infoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(infoLabel, BorderLayout.CENTER); // add label to the panel
    }

    /**
     * Sets the currently selected animal and updates the display.
     * @param animal The animal to display information about.
     */
    public void setSelectedAnimal(Animal animal) {
        this.selectedAnimal = animal; // set the selected animal
        updateInfo(); // update the displayed information
    }

    /**
     * Updates the information display about the selected animal.
     */
    public void updateInfo() {
        if(selectedAnimal == null) {
            infoLabel.setText("No animal selected"); // default text when no animal is selected
        } else {
            String info = String.format("%s | Energy: %d/%d | Age: %d/%d ", selectedAnimal.getId(), selectedAnimal.getEnergy(), Animal.MAX_ENERGY, selectedAnimal.getAge(), selectedAnimal.getMaxAge()); // create info with animal details
            infoLabel.setText(info); // set the formatted text
        }
    }

    /**
     * Gets the highlight color for selected animals.
     * @return The highlight color
     */
    public Color getHighlightColor() {
        return highlightColor;
    }

    /**
     * Displays a message indicating the selected animal is dead.
     */
    public void showAnimalDead() {
        infoLabel.setText("Animal is dead");
        selectedAnimal = null;
    }
}
