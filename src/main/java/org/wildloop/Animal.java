package org.wildloop;

/**
 * Abstract base class representing an animal in the simulation.
 * Defines basic behaviors and attributes common to all animals,
 * such as movement, reproduction, eating and energy management.
 *
 * @see Prey
 * @see Predator
 */
public abstract class Animal {
    /** Maximum energy level */
    protected static final int MAX_ENERGY = SimulationConfig.getIntValue("animal.max.energy");
    /** Default energy level for new animals */
    protected static final int DEFAULT_ENERGY = SimulationConfig.getIntValue("animal.default.energy");
    /** Energy cost of a single move */
    protected static final int MOVE_ENERGY_COST = SimulationConfig.getIntValue("animal.move.energy.cost");
    /** Energy threshold required for reproduction */
    protected static final int REPRODUCTION_ENERGY_THRESHOLD = SimulationConfig.getIntValue("animal.reproduction.energy.threshold");
    /** Energy cost of reproduction */
    protected static final int REPRODUCTION_ENERGY_COST = SimulationConfig.getIntValue("animal.reproduction.energy.cost");
    /** Initial energy of offspring */
    protected static final int OFFSPRING_ENERGY = SimulationConfig.getIntValue("animal.offspring.energy");

    /** Current position of the animal in the world */
    private Position position;
    /** Current energy level of the animal */
    private int energy;
    /** Current age of the animal */
    private int age;
    /** Maximum age after which the animal die */
    private final int maxAge;
    /** Flag indicating whether the animal is dead */
    private boolean isDead = false;
    /** Reference to the world in which the animal lives */
    protected World world;
    /** Unique identifier for the animal instance */
    private final String id;

    /**
     * Creates a new animal with given initial parameters.
     *
     * @param world    reference to the world where the animal lives
     * @param position initial position
     */
    public Animal(World world, Position position) {
        if (world == null) {
            throw new IllegalArgumentException("World cannot be null");
        }
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }

        this.world = world;
        this.position = position;
        this.energy = DEFAULT_ENERGY;
        this.maxAge = this instanceof Prey
                ? Prey.MAX_AGE
                : Predator.MAX_AGE;
        this.age = 0;
        this.id = generateUniqueId();

        world.addAnimal(this);
    }

    /**
     * Generates a unique identifier for the animal.
     * @return unique identifier as a string
     */
    protected abstract String generateUniqueId();

    /**
     * Returns the unique identifier of the animal.
     * @return unique identifier of the animal
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the current position of the animal in the world.
     * @return current position of the animal
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Returns the current energy level of the animal.
     * @return current energy level of the animal
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * Returns the current age of the animal.
     * @return current age of the animal
     */
    public int getAge() {
        return age;
    }

    /**
     * Returns the maximum age of the animal.
     * @return maximum age of the animal
     */
    public int getMaxAge() {
        return maxAge;
    }

    /**
     * Sets the current position of the animal.
     * @param position new position of the animal
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Sets the current energy level of the animal.
     * @param energy new energy level of the animal
     */
    public void setEnergy(int energy) {
        this.energy = Math.min(energy, MAX_ENERGY);
    }

    /**
     * Increments the age of the animal by one year.
     */
    public void incrementAge() {
        this.age++;
    }

    /**
     * Moves the animal in the direction determined by {@link #getNextMoveDirection()}.
     * Movement is only performed if the new position is valid and empty.
     * Each move costs energy specified by {@link #MOVE_ENERGY_COST}.
     */
    void move() {
        Direction direction = getNextMoveDirection();
        if (direction != null) {
            Position newPosition = position.newPosition(direction);
            if (world.isValidPosition(newPosition) && world.isCellEmpty(newPosition)) {
                Position oldPosition = position;
                world.getGrid()[oldPosition.x()][oldPosition.y()] = null;
                world.getGrid()[newPosition.x()][newPosition.y()] = this;
                position = newPosition;
                Event.log(EventType.MOVE, world, this);

                energy -= MOVE_ENERGY_COST;
            }
        }
    }

    /**
     * Determines the direction of the next animal movement.
     * Implementation depends on the specific animal type.
     *
     * @return movement direction or {@code null} if the animal cannot move
     */
    protected abstract Direction getNextMoveDirection();

    /**
     * Tries to reproduce the animal if it has enough energy.
     * Offspring is created on an empty adjacent cell.
     */
    void reproduce() {
        if (energy >= REPRODUCTION_ENERGY_THRESHOLD) {
            Position offspring_position = findEmptyAdjacentCell();
            if (offspring_position != null) {
                Animal offspring = createOffspring(offspring_position);
                offspring.setEnergy(OFFSPRING_ENERGY);
                Event.log(EventType.REPRODUCE, world, this, offspring);

                energy -= REPRODUCTION_ENERGY_COST;
            }
        }
    }

    /**
     * Creates a new animal of the same type.
     * Implementation depends on the specific animal type.
     *
     * @param position position for the new animal
     * @return new animal
     */
    protected abstract Animal createOffspring(Position position);

    /**
     * Finds an empty cell adjacent to the current animal position.
     *
     * @return position of empty adjacent cell or {@code null} if none found
     */
    private Position findEmptyAdjacentCell() {
        for (Direction dir : Direction.values()) {
            Position newPos = position.newPosition(dir);
            if (world.isCellEmpty(newPos)) {
                return newPos;
            }
        }
        return null;
    }

    /**
     * Defines the feeding behavior of the animal.
     * Implementation depends on the specific animal type.
     */
    protected abstract void eat();

    /**
     * Removes the animal from the world (animal dies).
     */
    public void die() {
        world.removeAnimal(this);
        energy = -1;
        isDead = true;
    }

    /**
     * Updates the animal's state in each simulation turn.
     * Performs the following actions:
     * <ol>
     * <li>Increases age</li>
     * <li>Makes a move</li>
     * <li>Tries to find food</li>
     * <li>Tries to reproduce if it has enough energy</li>
     * <li>Dies if runs out of energy or exceeds maximum age</li>
     * </ol>
     */
    public void update() {
        if (world == null) {
            return;
        }

        incrementAge();
        move();
        eat();

        if (energy >= REPRODUCTION_ENERGY_THRESHOLD) {
            reproduce();
        } else {
            eat();
        }

        if (energy <= 0 && !isDead) {
            Event.log(EventType.DIE_ENERGY, world, this);
            die();
        }

        if (age >= maxAge && !isDead) {
            Event.log(EventType.DIE_AGE, world, this);
            die();
        }
    }
}