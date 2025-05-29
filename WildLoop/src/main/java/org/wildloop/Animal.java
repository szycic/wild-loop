package org.wildloop;

public abstract class Animal {
    protected static final int MOVE_ENERGY_COST = SimulationConfig.getValue("animal.move.energy.cost");
    protected static final int REPRODUCTION_ENERGY_THRESHOLD = SimulationConfig.getValue("animal.reproduction.energy.threshold");
    protected static final int REPRODUCTION_ENERGY_COST = SimulationConfig.getValue("animal.reproduction.energy.cost");

    private Position position;
    private int energy;
    private int age;
    private final int maxAge;

    protected World world;

    public Animal(Position position, int energy, int maxAge) {
        this.position = position;
        this.energy = energy;
        this.maxAge = maxAge;
        this.age = 0;
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
        if (direction != null) {
            Position newPosition = position.move(direction);
            if (world.isValidPosition(newPosition) && world.isCellEmpty(newPosition)) {
                Position oldPosition = position;
                world.getGrid()[oldPosition.getX()][oldPosition.getY()] = null;
                world.getGrid()[newPosition.getX()][newPosition.getY()] = this;
                position = newPosition;
                energy -= MOVE_ENERGY_COST;
            }
        }
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
        if (world == null) {
            return;
        }

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
