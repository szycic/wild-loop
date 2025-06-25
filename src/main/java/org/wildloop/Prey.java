package org.wildloop;

/**
 * Represents prey in the simulation. Prey flees from Predator objects
 * within a specified range and gains energy through grazing.
 *
 * @see Animal
 * @see Predator
 */
public class Prey extends Animal {
    /** Maximum age of prey after which it dies */
    protected static final int MAX_AGE = SimulationConfig.getIntValue("prey.max.age");
    /** Maximum range at which prey can detect predator */
    protected static final int FLEE_RANGE = SimulationConfig.getIntValue("prey.flee.range");
    /** Amount of energy gained by prey while grazing */
    protected static final int GRAZE_ENERGY_GAIN = SimulationConfig.getIntValue("prey.graze.energy.gain");

    /**
     * A static counter used to generate unique identifiers for prey instances.
     * Increments with each new prey created to ensure ID uniqueness.
     */
    private static long idCounter = 0;

    /**
     * Creates new prey.
     *
     * @param world    reference to the world where the prey lives
     * @param position initial position of the prey
     */
    public Prey(World world, Position position) {
        super(world, position);
    }

    /**
     * Generates a unique identifier for the prey instance.
     * The ID is prefixed with "PREY-" followed by an incrementing counter.
     *
     * @return unique identifier for the prey
     */
    @Override
    protected String generateUniqueId() {
        return "PREY-" + ++idCounter;
    }

    /**
     * Determines the direction of prey's next move.
     * If a predator is within {@link #FLEE_RANGE}, prey flees in opposite direction.
     * Otherwise, choose a random direction.
     *
     * @return direction in which prey should move
     */
    @Override
    protected Direction getNextMoveDirection() {
        Predator nearestPredator = findNearestPredator();
        if (nearestPredator != null) {
            Event.log(EventType.FLEE, world, this, nearestPredator);
            return getPosition().directionFrom(nearestPredator.getPosition());
        }
        return Direction.getRandom();
    }

    /**
     * Finds the nearest predator within prey's detection range.
     *
     * @return nearest predator or {@code null} if none is within range
     */
    private Predator findNearestPredator() {
        if (getPosition() == null) {
            throw new IllegalStateException("Prey has no position defined");
        }

        Predator nearestPredator = null;
        int minDistance = Integer.MAX_VALUE;

        for (Animal animal : world.getAnimals()) {
            if (animal instanceof Predator) {
                int distance = getPosition().distanceTo(animal.getPosition());
                if (distance <= FLEE_RANGE && distance < minDistance) {
                    nearestPredator = (Predator) animal;
                    minDistance = distance;
                }
            }
        }

        return nearestPredator;
    }

    /**
     * Allows prey to graze, increasing its energy by {@link #GRAZE_ENERGY_GAIN}.
     */
    @Override
    protected void eat() {
        if (getEnergy() <= MAX_ENERGY - GRAZE_ENERGY_GAIN) {
            Event.log(EventType.EAT_GRASS, world, this);
            setEnergy(getEnergy() + GRAZE_ENERGY_GAIN);
        }
    }

    /**
     * Creates new prey as offspring.
     *
     * @param position position for the new prey
     * @return new prey object with initial energy equal to {@link #OFFSPRING_ENERGY}
     */
    @Override
    protected Animal createOffspring(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Offspring position cannot be null");
        }

        return new Prey(world, position);
    }
}