package org.wildloop;

public abstract class Animal {
    protected static final int MOVE_ENERGY_COST = 1;
    protected static final int REPRODUCTION_ENERGY_THRESHOLD = 50;
    protected static final int REPRODUCTION_ENERGY_COST = 30;

    protected World world;

    private Position position;
    private int energy;
    private int age;
    private int maxAge;

    public Animal(Position position, int energy, int maxAge) {
        this.position = position;
        this.energy = energy;
        this.maxAge = maxAge;
        this.age = 0;
    }
    public Animal(Animal animal) {
        this(animal.position, animal.energy, animal.maxAge);
    }
    public Animal() {
        this(new Position(), 100, 10);
    }
    
    public Position getPosition() {
        return position;
    }
    public int getEnergy() {
        return energy;
    }
    public int getAge() {
        return age;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
    public void setEnergy(int energy) {
        this.energy = energy;
    }
    public void incrementAge() {
        this.age++;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    void move() {
        Direction direction = getNextMoveDirection();
    }
    protected abstract Direction getNextMoveDirection();

    void reproduce() {
        if (energy >= REPRODUCTION_ENERGY_THRESHOLD) {
            Position offspring_position = findEmptyAdjacentCell();
            if (offspring_position != null) {
                Animal offspring = createOffspring(offspring_position);
                energy -= REPRODUCTION_ENERGY_COST;
                offspring.setEnergy(REPRODUCTION_ENERGY_COST);
                world.addAnimal(offspring);
            }
        }
    }
    protected abstract Animal createOffspring(Position position);
    private Position findEmptyAdjacentCell() {
        for (Direction dir : Direction.values()) {
            Position newPos = position.move(dir);
            if (world.isCellEmpty(newPos)) {
                return newPos;
            }
        }
        return null;
    }

    protected abstract void eat();

    public void die() {
        world.removeAnimal(this);
    }

    public void update() {
        incrementAge();
        move();
        eat();
        if (energy >= REPRODUCTION_ENERGY_THRESHOLD) {
            reproduce();
        }
        if (energy <= 0 || age >= maxAge) {
            die();
        }
    }
}
