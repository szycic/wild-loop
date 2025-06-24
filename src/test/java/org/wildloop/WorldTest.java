package org.wildloop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WorldTest {

    @Test
    public void EventIsNull() {
        EventType type = EventType.SIMULATION_START;

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> Event.log(type, null)
        );

        assertEquals("World cannot be null", exception.getMessage());
    }

    @Test
    public void AddNullAnimal() {
        World world = new World(10, 10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> world.addAnimal(null));

        assertEquals("Cannot add null animal", exception.getMessage());
    }

    @Test
    public void AddAnimalInvalidPosition() {
        World world = new World(10, 10);
        Position invalidPosition = new Position(15, 15);
        Animal animal = new Predator(world, invalidPosition);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> world.addAnimal(animal));

        assertEquals("Invalid position", exception.getMessage());
    }

    @Test
    public void AddAnimalOccupiedCell() {
        World world = new World(10, 10);
        Position position = new Position(5, 5);
        Animal firstAnimal = new Predator(world, position);
        world.addAnimal(firstAnimal);
        Animal secondAnimal = new Predator(world, position);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> world.addAnimal(secondAnimal));

        assertEquals("Selected cell is already occupied", exception.getMessage());
    }

    @Test
    void RemoveNullAnimal() {
        World world = new World(10, 10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> world.removeAnimal(null));

        assertEquals("Cannot remove null animal", exception.getMessage());
    }

    @Test
    void RemoveAnimalNotInWorld() {
        World world = new World(10, 10);
        Position position = new Position(5, 5);
        Animal animal = new Predator(world, position);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> world.removeAnimal(animal));

        assertEquals("Animal does not exist in the world", exception.getMessage());
    }

    @Test
    void RemoveAnimalGridMismatch() {
        World world = new World(10, 10);
        Animal animal = new Predator(world, new Position(5, 5));
        world.getAnimals().add(animal);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> world.removeAnimal(animal));

        assertEquals("Invalid animal position or grid position mismatch", exception.getMessage());
    }

    @Test
    void getValueInvalidValue() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> SimulationConfig.getValue("Invalid value"));

        String expectedMessage = "Value 'null' for parameter 'Invalid value' is not a valid integer";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void PreyNullPosition() throws Exception {
        Position position = new Position(5, 5);
        World world = new World(10, 10);
        Prey prey = new Prey(world, position);

        java.lang.reflect.Field positionField = Animal.class.getDeclaredField("position");
        positionField.setAccessible(true);
        positionField.set(prey, null);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                prey::getNextMoveDirection);

        assertEquals("Prey has no position defined", exception.getMessage());
    }

    @Test
    void PreyOffspringNullPosition() {
        World world = new World(10, 10);
        Prey parent = new Prey(world, new Position(1, 1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> parent.createOffspring(null));

        assertEquals("Offspring position cannot be null", exception.getMessage());
    }

    @Test
    void PredatorNullPosition() throws Exception {
        World world = new World(10, 10);
        Position position = new Position(5, 5);
        Predator predator = new Predator(world, position);

        predator.setEnergy(Animal.MAX_ENERGY - Predator.REPRODUCTION_ENERGY_COST - 1);

        java.lang.reflect.Field positionField = Animal.class.getDeclaredField("position");
        positionField.setAccessible(true);
        positionField.set(predator, null);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                predator::getNextMoveDirection);

        assertEquals("Predator has no position", exception.getMessage());
    }

    @Test
    void PredatorOffspringNullPosition() {
        World world = new World(10, 10);
        Predator parent = new Predator(world, new Position(1, 1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> parent.createOffspring(null));

        assertEquals("Offspring position cannot be null", exception.getMessage());
    }


}