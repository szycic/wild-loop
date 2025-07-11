package org.wildloop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PreyTest {
    private World world;
    private Position position;
    private Prey prey;

    @BeforeEach
    void setUp() {
        world = new World(10, 10); // Create a world of size 10x10
        position = new Position(5, 5); // Set the initial position for the prey
        prey = new Prey(world, position); // Create a new Prey instance
    }

    @Test
    void testPreyInitialization() {
        assertNotNull(prey); // Ensure prey is initialized
        assertNotNull(prey.getId()); // Ensure the prey has a unique ID
        Assertions.assertTrue(prey.getId().startsWith("PREY-")); // Check if ID starts with "PREY-"
        assertEquals(position, prey.getPosition());
        assertEquals(Prey.DEFAULT_ENERGY, prey.getEnergy());
        assertEquals(Prey.MAX_AGE, prey.getMaxAge()); // Ensure max age is set correctly
        assertEquals(0, prey.getAge()); // Initial age should be 0
    }

    @Test
    void testPreyEating() {
        prey.setEnergy(Prey.DEFAULT_ENERGY - Prey.GRAZE_ENERGY_GAIN); // Set energy to just below max
        assertEquals(Prey.DEFAULT_ENERGY - Prey.GRAZE_ENERGY_GAIN, prey.getEnergy()); // Ensure energy is below default
        prey.eat(); // Simulate grazing to gain energy
        assertEquals(Prey.DEFAULT_ENERGY, prey.getEnergy()); // Check if energy is restored to default after grazing
    }

    @Test
    void testPreyFleeing() {
        new Predator(world, new Position(6, 5)); // Create a predator near the prey
        Direction fleeDirection = prey.getNextMoveDirection(); // Get the direction in which prey should flee
        assertNotNull(fleeDirection); // Ensure flee direction is not null
        assertEquals(Direction.WEST, fleeDirection); // Assuming the prey flees west from the predator's east position
    }

    @Test
    void testPreyDeath() {
        prey.die(); // Simulate death
        assertEquals(-1, prey.getEnergy()); // Ensure energy is set to -1 after death
        assertFalse(world.getAnimals().contains(prey)); // Ensure prey is removed from the world after death
    }

    @Test
    void testPreyReproduction() {
        prey.setEnergy(Prey.REPRODUCTION_ENERGY_THRESHOLD); // Set energy to the threshold for reproduction
        int initialEnergy = prey.getEnergy(); // Store initial energy before reproduction

        prey.reproduce(); // Simulate reproduction

        assertEquals(initialEnergy - Prey.REPRODUCTION_ENERGY_COST, prey.getEnergy()); // Check if energy is reduced correctly
        assertEquals(2, world.getAnimals().stream().filter(a -> a instanceof Prey && a.getId().startsWith("PREY-")).count()); // Ensure not more than one prey is created
    }

    @Test
    void testPreyReproductionWithEnergyBelowThreshold() {
        prey.setEnergy(Prey.REPRODUCTION_ENERGY_THRESHOLD - 1); // Set energy below reproduction threshold
        int initialEnergy = prey.getEnergy(); // Store initial energy before reproduction

        prey.reproduce(); // Attempt reproduction

        assertEquals(initialEnergy, prey.getEnergy()); // Ensure energy remains unchanged
        assertEquals(1, world.getAnimals().stream().filter(a -> a instanceof Prey && a.getId().startsWith("PREY-")).count()); // Ensure not more than one prey is created
    }
}
