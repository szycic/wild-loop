package org.wildloop;

import java.util.List;

public class World {
    private Animal[][] grid;
    private List<Animal> animals;
    private int turns;
    
    public World(int width, int height) {
        this.grid = new Animal[width][height];
        this.animals = List.of();
        this.turns = 0;
    }
    public World(World world) {
        this(world.grid.length, world.grid[0].length);
        this.grid = world.grid.clone();
        this.animals = List.copyOf(world.animals);
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
    
    public void addAnimal() {}
    public void removeAnimal() {}
    public void tick() {
        turns++;
    }
}
