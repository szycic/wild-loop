package org.wildloop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PredatorTest {
    private World world;
    private Position position;
    private Predator predator;

    @BeforeEach
    void setUp() {
        world = new World(10, 10); // Create a world of size 10x10
        position = new Position(5, 5); // Set the initial position for the predator
        predator = new Predator(world, position); // Create a new Predator instance
    }

    @Test
    void testPredatorInitialization() {
        assertNotNull(predator); // Ensure predator is initialized
        assertNotNull(predator.getId()); // Ensure the predator has a unique ID
        assertTrue(predator.getId().startsWith("PREDATOR-")); // Check if ID starts with "PREDATOR-"
        assertEquals(position, predator.getPosition());
        assertEquals(Predator.DEFAULT_ENERGY, predator.getEnergy());
        assertEquals(Predator.MAX_AGE, predator.getMaxAge()); // Ensure max age is set correctly
        assertEquals(0, predator.getAge()); // Initial age should be 0
    }

    @Test
    void testPredatorEating() {
        new Prey(world, new Position(6, 5)); // Create prey near the predator
        predator.setEnergy(Predator.DEFAULT_ENERGY - Predator.HUNT_ENERGY_GAIN); // Set energy to just below max
        assertEquals(Predator.DEFAULT_ENERGY - Predator.HUNT_ENERGY_GAIN, predator.getEnergy()); // Ensure energy is below default
        predator.eat(); // Simulate eating to gain energy
        assertEquals(Predator.DEFAULT_ENERGY, predator.getEnergy()); // Check if energy is restored to default after eating prey
    }

    @Test
    void testPredatorHunting() {
        new Prey(world, new Position(6, 5)); // Create prey near the predator
        predator.setEnergy(Predator.DEFAULT_ENERGY - Predator.HUNT_ENERGY_GAIN); // Set energy to just below max
        Direction huntDirection = predator.getNextMoveDirection(); // Get the direction in which predator should move
        assertEquals(Direction.EAST, huntDirection); // Assuming the predator moves east towards the prey's position
    }

    @Test
    void testPredatorDeath() {
        predator.die(); // Simulate death
        assertEquals(-1, predator.getEnergy()); // Ensure energy is set to -1 after death
        assertFalse(world.getAnimals().contains(predator)); // Ensure predator is removed from the world after death
    }

    @Test
    void testPredatorReproduction() {
        predator.setEnergy(Predator.REPRODUCTION_ENERGY_THRESHOLD); // Set energy to the threshold for reproduction
        int initialEnergy = predator.getEnergy(); // Store initial energy before reproduction

        predator.reproduce(); // Simulate reproduction

        assertEquals(initialEnergy - Predator.REPRODUCTION_ENERGY_COST, predator.getEnergy()); // Check if energy is reduced correctly
        assertEquals(2, world.getAnimals().stream().filter(a -> a instanceof Predator && a.getId().startsWith("PREDATOR-")).count()); // Ensure not more than one prey is created
    }

    @Test
    void testPredatorReproductionWithEnergyBelowThreshold() {
        predator.setEnergy(Predator.REPRODUCTION_ENERGY_THRESHOLD - 1); // Set energy below reproduction threshold
        int initialEnergy = predator.getEnergy(); // Store initial energy before reproduction

        predator.reproduce(); // Attempt reproduction

        assertEquals(initialEnergy, predator.getEnergy()); // Ensure energy remains unchanged
        assertEquals(1, world.getAnimals().stream().filter(a -> a instanceof Predator && a.getId().startsWith("PREDATOR-")).count()); // Ensure not more than one prey is created
    }
}
