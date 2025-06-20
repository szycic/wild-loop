package org.wildloop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    /** List of events that occurred in the world */
    private List<Event> events;

    /**
     * Creates a new world with specified dimensions.
     *
     * @param width  width of the world (number of cells)
     * @param height height of the world (number of cells)
     */
    public World(int width, int height) {
        this.grid = new Animal[width][height];
        this.animals = new ArrayList<>();
        this.turn = 0;
        this.events = new ArrayList<>();
    }

    /** @return current world grid with animals */
    public Animal[][] getGrid() {
        return grid;
    }

    /** @return list of all living animals */
    public List<Animal> getAnimals() {
        return animals;
    }

    /** @return number of completed turns */
    public int getTurn() {
        return turn;
    }

    /** @return width of the world */
    public int getWidth() {
        return grid.length;
    }

    /** @return height of the world */
    public int getHeight() {
        return grid[0].length;
    }

    /** @return unmodifiable list of all events that occurred in the world */
    public List<Event> getEvents() {
        return Collections.unmodifiableList(events);
    }

    /**
     * Adds a new event to the world.
     *
     * @param event event to add
     * @throws IllegalArgumentException if event is null
     */
    public void addEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Cannot add null event");
        }
        events.add(event);
    }

    /**
     * Adds a new animal to the world.
     *
     * @param animal animal to add
     * @throws IllegalArgumentException if animal is null or its position is invalid
     * @throws IllegalStateException    if target cell is already occupied
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
        animal.setWorld(this);
    }

    /**
     * Removes an animal from the world.
     *
     * @param animal animal to remove
     * @throws IllegalArgumentException if animal is null
     * @throws IllegalStateException    if animal does not exist in the world or its position is invalid
     */
    public void removeAnimal(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Cannot remove null animal");
        }

        Position position = animal.getPosition();
        if (!animals.contains(animal)) {
            throw new IllegalStateException("Animal does not exist in the world");
        }
        if (!isValidPosition(position) || grid[position.x()][position.y()] != animal) {
            throw new IllegalStateException("Invalid animal position or grid position mismatch");
        }

        grid[position.x()][position.y()] = null;
        animals.remove(animal);
        animal.setWorld(null);
        animal.setPosition(null);
        animal.setEnergy(-1);
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
            animal.update();
        }
        turn++;
    }

    /**
     * Resets the simulation world.
     */
    public void resetWorld() {
        this.grid = new Animal[getWidth()][getHeight()];
        this.animals = new ArrayList<>();
        this.turn = 0;
    }
}