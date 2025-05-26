package org.wildloop;

import java.util.ArrayList;
import java.util.List;

public class World {
    private Animal[][] grid;
    private List<Animal> animals;
    private int turns;
    
    public World(int width, int height) {
        this.grid = new Animal[width][height];
        this.animals = new ArrayList<>();
        this.turns = 0;
    }
    public World(World world) {
        this(world.grid.length, world.grid[0].length);
        this.grid = world.grid.clone();
        this.animals = new ArrayList<>(world.animals);
        this.turns = world.turns;
    }
    public World() {
        this(10, 10);
    }
    
    public Animal[][] getGrid() {
        return grid;
    }
    public List<Animal> getAnimals() {
        return animals;
    }
    public int getTurns() {
        return turns;
    }
    public int getWidth() {
        return grid.length;
    }
    public int getHeight() {
        return grid[0].length;
    }
    
    public void addAnimal(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Cannot add null animal");
        }

        Position position = animal.getPosition();
        if (!isValidPosition(position)) {
            throw new IllegalArgumentException("Invalid position");
        }
        if (!isCellEmpty(position)) {
            throw new IllegalStateException("Cell is not empty");
        }

        grid[position.getX()][position.getY()] = animal;
        animals.add(animal);

        animal.setWorld(this);
    }
    public void removeAnimal(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Cannot remove null animal");
        }

        Position position = animal.getPosition();
        if (!animals.contains(animal)) {
            throw new IllegalStateException("Animal is not in the world");
        }
        if (!isValidPosition(position) || grid[position.getX()][position.getY()] != animal) {
            throw new IllegalStateException("Animal position is invalid or grid position mismatch");
        }

        grid[position.getX()][position.getY()] = null;
        animals.remove(animal);

        animal.setWorld(null);
        animal.setPosition(null);
        animal.setEnergy(-1);
    }

    public boolean isValidPosition(Position position) {
        return position.getX() >= 0 && position.getX() < getWidth()
                && position.getY() >= 0 && position.getY() < getHeight();
    }
    public boolean isCellEmpty(Position position) {
        return isValidPosition(position) && grid[position.getX()][position.getY()] == null;
    }


    public void tick() {
        List<Animal> currentAnimals = new ArrayList<>(animals);
        for (Animal animal : currentAnimals) {
            animal.update();
        }
        turns++;
    }
}
