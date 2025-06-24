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
    protected static final int MAX_ENERGY = SimulationConfig.getValue("animal.max.energy");
    /** Energy cost of a single move */
    protected static final int MOVE_ENERGY_COST = SimulationConfig.getValue("animal.move.energy.cost");
    /** Energy threshold required for reproduction */
    protected static final int REPRODUCTION_ENERGY_THRESHOLD = SimulationConfig.getValue("animal.reproduction.energy.threshold");
    /** Energy cost of reproduction */
    protected static final int REPRODUCTION_ENERGY_COST = SimulationConfig.getValue("animal.reproduction.energy.cost");
    /** Initial energy of offspring */
    protected static final int OFFSPRING_ENERGY = SimulationConfig.getValue("animal.offspring.energy");

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
     * @param position initial position
     * @param energy   initial energy level
     * @param maxAge   maximum age the animal can reach
     */
    public Animal(World world, Position position, int energy, int maxAge) {
        if (world == null) {
            throw new IllegalArgumentException("World cannot be null");
        }
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        if (energy < 0) {
            throw new IllegalArgumentException("Initial energy cannot be negative");
        }
        if (energy > MAX_ENERGY) {
            throw new IllegalArgumentException("Initial energy cannot be greater than maximum energy");
        }
        if (maxAge <= 0) {
            throw new IllegalArgumentException("Maximum age must be greater than zero");
        }

        this.world = world;
        this.position = position;
        this.energy = energy;
        this.maxAge = maxAge;
        this.age = 0;
        this.id = generateUniqueId();

        world.addAnimal(this);
    }

    /** @return unique identifier for the animal */
    protected abstract String generateUniqueId();

    /** @return unique identifier of the animal */
    public String getId() {
        return id;
    }

    /** @return current position of the animal */
    public Position getPosition() {
        return position;
    }

    /** @return current energy level of the animal */
    public int getEnergy() {
        return energy;
    }

    /** @return current age of the animal */
    public int getAge() {
        return age;
    }

    /** @return maximum age that the animal can reach */
    public int getMaxAge() {
        return maxAge;
    }

    /** @param position new position of the animal */
    public void setPosition(Position position) {
        this.position = position;
    }

    /** @param energy new energy level */
    public void setEnergy(int energy) {
        this.energy = Math.min(energy, MAX_ENERGY);
    }

    /** Increases animal age by 1 */
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