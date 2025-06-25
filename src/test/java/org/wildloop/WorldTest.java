package org.wildloop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WorldTest {
    @Test
    public void AddNullAnimal() {
        World world = new World(10, 10); // Create a world of size 10x10

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> world.addAnimal(null)); // Attempt to add a null animal

        assertEquals("Cannot add null animal", exception.getMessage()); // Check if the exception message is as expected
    }

    @Test
    public void AddAnimalInvalidPosition() {
        World world = new World(10, 10); // Create a world of size 10x10
        Position invalidPosition = new Position(15, 15); // Position outside the world boundaries

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Predator(world, invalidPosition)); // Attempt to create a predator at an invalid position

        assertEquals("Invalid position", exception.getMessage()); // Check if the exception message is as expected
    }

    @Test
    public void AddAnimalOccupiedCell() {
        World world = new World(10, 10); // Create a world of size 10x10
        Position position = new Position(5, 5); // Valid position within the world
        new Predator(world, position); // Create a predator at the position

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> new Predator(world, position)); // Attempt to create another predator at the same position

        assertEquals("Selected cell is already occupied", exception.getMessage()); // Check if the exception message is as expected
    }

    @Test
    void RemoveNullAnimal() {
        World world = new World(10, 10); // Create a world of size 10x10

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> world.removeAnimal(null)); // Attempt to remove a null animal

        assertEquals("Cannot remove null animal", exception.getMessage()); // Check if the exception message is as expected
    }

    @Test
    void RemoveAnimalNotInWorld() {
        World world = new World(10, 10); // Create a world of size 10x10
        Position position = new Position(5, 5); // Valid position within the world
        Animal animal = new Predator(world, position); // Create a predator at the position
        world.removeAnimal(animal); // Remove the animal from the world

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> world.removeAnimal(animal)); // Attempt to remove the same animal again

        assertEquals("Animal does not exist in the world", exception.getMessage()); // Check if the exception message is as expected
    }

    @Test
    void RemoveAnimalGridMismatch() {
        World world = new World(10, 10); // Create a world of size 10x10
        Animal animal = new Predator(world, new Position(5, 5)); // Create a predator at position (5, 5)

        world.getGrid()[5][5] = null; // Simulate a grid mismatch by removing the animal from the grid

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> world.removeAnimal(animal)); // Attempt to remove the animal

        assertEquals("Invalid animal position or grid position mismatch", exception.getMessage()); // Check if the exception message is as expected
    }

    @Test
    void PreyNullPosition() throws Exception {
        Position position = new Position(5, 5); // Create a valid position
        World world = new World(10, 10); // Create a world of size 10x10
        Prey prey = new Prey(world, position); // Create a new Prey instance

        java.lang.reflect.Field positionField = Animal.class.getDeclaredField("position"); // Access the private position field
        positionField.setAccessible(true); // Make the field accessible
        positionField.set(prey, null); // Set the position to null

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                prey::getNextMoveDirection); // Attempt to get the next move direction

        assertEquals("Prey has no position defined", exception.getMessage()); // Check if the exception message is as expected
    }

    @Test
    void PreyOffspringNullPosition() {
        World world = new World(10, 10); // Create a world of size 10x10
        Prey parent = new Prey(world, new Position(1, 1)); // Create a new Prey instance at position (1, 1)

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> parent.createOffspring(null)); // Attempt to create an offspring with a null position

        assertEquals("Offspring position cannot be null", exception.getMessage()); // Check if the exception message is as expected
    }

    @Test
    void PredatorNullPosition() throws Exception {
        World world = new World(10, 10); // Create a world of size 10x10
        Position position = new Position(5, 5); // Create a valid position
        Predator predator = new Predator(world, position); // Create a new Predator instance

        predator.setEnergy(Animal.MAX_ENERGY - Predator.REPRODUCTION_ENERGY_COST - 1); // Set energy just below reproduction threshold

        java.lang.reflect.Field positionField = Animal.class.getDeclaredField("position"); // Access the private position field
        positionField.setAccessible(true); // Make the field accessible
        positionField.set(predator, null); // Set the position to null

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                predator::getNextMoveDirection); // Attempt to get the next move direction

        assertEquals("Predator has no position", exception.getMessage()); // Check if the exception message is as expected
    }

    @Test
    void PredatorOffspringNullPosition() {
        World world = new World(10, 10); // Create a world of size 10x10
        Predator parent = new Predator(world, new Position(1, 1)); // Create a new Predator instance at position (1, 1)

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> parent.createOffspring(null)); // Attempt to create an offspring with a null position

        assertEquals("Offspring position cannot be null", exception.getMessage()); // Check if the exception message is as expected
    }

    @Test
    void WorldDimensions() {
        World world = new World(10, 10); // Create a world of size 10x10

        assertEquals(10, world.getWidth()); // Check if the width is correct
        assertEquals(10, world.getHeight()); // Check if the height is correct
        assertEquals(100, world.getGrid().length * world.getGrid()[0].length); // Check if the grid size is correct
    }

    @Test
    void WorldIdGeneration() {
        World world1 = new World(10, 10); // Create a world of size 10x10
        World world2 = new World(10, 10); // Create another world of the same size

        assertNotEquals(world1.getId(), world2.getId()); // Check if the IDs are unique
    }

    @Test
    void AddAndRemoveMultipleAnimals() {
        World world = new World(10, 10); // Create a world of size 10x10
        Position position1 = new Position(2, 2); // Valid position for the first animal
        Position position2 = new Position(3, 3); // Valid position for the second animal

        Animal animal1 = new Predator(world, position1); // Create the first predator
        Animal animal2 = new Prey(world, position2); // Create the second prey

        assertTrue(world.getAnimals().contains(animal1)); // Check if the first animal is in the world
        assertTrue(world.getAnimals().contains(animal2)); // Check if the second animal is in the world
        assertEquals(2, world.getAnimals().size()); // Check if the number of animals is correct
        assertFalse(world.isCellEmpty(position1)); // Check if the cell is occupied by the first animal
        assertFalse(world.isCellEmpty(position2)); // Check if the cell is occupied by the second animal

        world.removeAnimal(animal1); // Remove the first animal from the world
        assertFalse(world.getAnimals().contains(animal1)); // Check if the first animal is removed
        assertEquals(1, world.getAnimals().size()); // Check if the number of animals is correct after removal
        assertTrue(world.isCellEmpty(position1)); // Check if the cell is empty after removal

        world.removeAnimal(animal2); // Remove the second animal from the world
        assertFalse(world.getAnimals().contains(animal2)); // Check if the second animal is removed
        assertEquals(0, world.getAnimals().size()); // Check if the number of animals is zero after both removals
        assertTrue(world.isCellEmpty(position2)); // Check if the cell is empty after removal
    }

    @Test
    void IsValidPositionAndIsCellEmpty() {
        World world = new World(10, 10); // Create a world of size 10x10
        Position validPosition = new Position(5, 5); // Valid position within the world
        Position invalidPosition = new Position(15, 15); // Invalid position outside the world boundaries

        assertTrue(world.isValidPosition(validPosition)); // Check if the valid position is recognized as valid
        assertFalse(world.isValidPosition(invalidPosition)); // Check if the invalid position is recognized as invalid

        assertTrue(world.isCellEmpty(validPosition)); // Check if the cell at the valid position is empty
        new Prey(world, validPosition); // Add an animal to the valid position
        assertFalse(world.isCellEmpty(validPosition)); // Check if the cell is no longer empty after adding an animal
    }

    @Test
    void GetAnimalsByType() {
        World world = new World(10, 10); // Create a world of size 10x10
        Position position1 = new Position(2, 2); // Valid position for the first animal
        Position position2 = new Position(3, 3); // Valid position for the second animal

        Animal predator = new Predator(world, position1); // Create a predator
        Animal prey = new Prey(world, position2); // Create a prey

        assertTrue(world.getAnimals().contains(predator)); // Check if the predator is in the world
        assertTrue(world.getAnimals().contains(prey)); // Check if the prey is in the world

        assertEquals(1, world.getAnimals().stream().filter(a -> a instanceof Predator).count()); // Check if there is one predator
        assertEquals(1, world.getAnimals().stream().filter(a -> a instanceof Prey).count()); // Check if there is one prey
    }

    @Test
    void ResetClearsAnimalsAndGrid() {
        World world = new World(10, 10); // Create a world of size 10x10
        Position position1 = new Position(2, 2); // Valid position for the first animal
        Position position2 = new Position(3, 3); // Valid position for the second animal

        Animal predator = new Predator(world, position1); // Create a predator
        Animal prey = new Prey(world, position2); // Create a prey

        assertTrue(world.getAnimals().contains(predator)); // Check if the predator is in the world
        assertTrue(world.getAnimals().contains(prey)); // Check if the prey is in the world

        world.reset(); // Reset the world

        // Check if the grid is cleared
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                assertNull(world.getGrid()[x][y]);
            }
        }

        assertTrue(world.getAnimals().isEmpty()); // Check if all animals are removed
        assertTrue(world.isCellEmpty(position1)); // Check if the cell at position1 is empty
        assertTrue(world.isCellEmpty(position2)); // Check if the cell at position2 is empty
    }
}