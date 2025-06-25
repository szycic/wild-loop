package org.wildloop;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents the simulation world where animals can move and interact.
 * The world is organized as a two-dimensional grid where each cell can contain one animal.
 *
 * @see Animal
 * @see Position
 * @see Direction
 */
public class World {
    /** Two-dimensional grid representing animal placement */
    private Animal[][] grid;
    /** List of all active animals in the world */
    private List<Animal> animals;
    /** Counter of completed simulation turns */
    private int turn;
    /** Unique identifier for the world instance */
    private final String id;

    /**
     * Creates a new world with specified dimensions.
     *
     * @param width  width of the world (number of cells)
     * @param height height of the world (number of cells)
     */
    public World(int width, int height) {
        this.grid = new Animal[width][height];
        this.animals = new ArrayList<>();
        this.turn = 1;
        this.id = UUID.randomUUID().toString().substring(0, 8);

        LogExporter.openLog(this.id);
        Event.log(EventType.SIMULATION_START, this);
    }

    /**
     * Returns the two-dimensional grid representing the world.
     * @return grid of animals in the world
     */
    public Animal[][] getGrid() {
        return grid;
    }

    /**
     * Returns a list of all animals currently in the world.
     * @return list of animals
     */
    public List<Animal> getAnimals() {
        return animals;
    }

    /**
     * Returns the current turn number in the simulation.
     * @return current turn number
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Returns the width of the world.
     * @return width of the world
     */
    public int getWidth() {
        return grid.length;
    }

    /**
     * Returns the height of the world.
     * @return height of the world
     */
    public int getHeight() {
        return grid[0].length;
    }

    /**
     * Returns the unique identifier of the world.
     * @return unique identifier of the world
     */
    public String getId() {
        return id;
    }

    /**
     * Adds a new animal to the world.
     *
     * @param animal animal to add
     * @throws IllegalArgumentException if an animal is null or its position is invalid
     * @throws IllegalStateException    if the target cell is already occupied
     */
    public void addAnimal(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Cannot add null animal");
        }

        Position position = animal.getPosition();
        if (!isValidPosition(position)) {
            throw new IllegalArgumentException("Invalid position");
        }
        if (!isCellEmpty(position)) {
            throw new IllegalStateException("Selected cell is already occupied");
        }

        grid[position.x()][position.y()] = animal;
        animals.add(animal);
        Event.log(EventType.SPAWN, this, animal);
    }

    /**
     * Removes an animal from the world.
     *
     * @param animal animal to remove
     * @throws IllegalArgumentException if the animal is null
     * @throws IllegalStateException    if an animal does not exist in the world or its position is invalid
     */
    public void removeAnimal(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Cannot remove null animal");
        }

        Position position = animal.getPosition();
        if (!animals.contains(animal)) {
            throw new IllegalStateException(animal.getId() + " does not exist in the world");
        }
        if (!isValidPosition(position)) {
            throw new IllegalStateException(animal.getId() + " position" + animal.getPosition() + " is invalid");
        }
        if (grid[position.x()][position.y()] != animal) {
            throw new IllegalStateException(animal.getId() + " position " + animal.getPosition() + " does not match the grid");
        }

        grid[position.x()][position.y()] = null;
        animals.remove(animal);
    }

    /**
     * Checks if a given position is within world boundaries.
     *
     * @param position position to check
     * @return true if the position is valid, false otherwise
     */
    public boolean isValidPosition(Position position) {
        return position.x() >= 0 && position.x() < getWidth()
                && position.y() >= 0 && position.y() < getHeight();
    }

    /**
     * Checks if cell at given position is empty.
     *
     * @param position position to check
     * @return true if the cell is empty, false otherwise
     */
    public boolean isCellEmpty(Position position) {
        return isValidPosition(position) && grid[position.x()][position.y()] == null;
    }

    /**
     * Executes one simulation turn, updating the state of all animals
     * and incrementing turn counter.
     */
    public void tick() {
        List<Animal> currentAnimals = new ArrayList<>(animals);
        for (Animal animal : currentAnimals) {
            if (!animal.isDead()) animal.update();
        }
        Event.log(EventType.SIMULATION_TURN, this);
        turn++;
    }

    /**
     * Resets the simulation world.
     */
    public void reset() {
        this.grid = new Animal[getWidth()][getHeight()];
        this.animals = new ArrayList<>();
        this.turn = 1;
    }
}