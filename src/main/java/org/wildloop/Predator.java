package org.wildloop;

/**
 * Represents predator in the simulation. Predator hunts for Prey objects
 * within a specified range and gains energy by eating them.
 *
 * @see Animal
 * @see Prey
 */
public class Predator extends Animal {
    /**
     * Maximum range at which a predator can detect prey
     */
    private static final int HUNT_RANGE = SimulationConfig.getValue("predator.hunt.range");
    /**
     * Amount of energy gained by predator after eating prey
     */
    private static final int HUNT_ENERGY_GAIN = SimulationConfig.getValue("predator.hunt.energy.gain");

    /**
     * A static counter used to generate unique identifiers for prey instances.
     * Increments with each new prey created to ensure ID uniqueness.
     */
    private static long idCounter = 0;

    /**
     * Creates a new predator.
     *
     * @param world    reference to the world where the predator lives
     * @param position initial position of the predator
     */
    public Predator(World world, Position position) {
        super(world, position);
    }

    /**
     * Generates a unique identifier for the prey instance.
     * The ID is prefixed with "PREDATOR-" followed by an incrementing counter.
     *
     * @return unique identifier for the prey
     */
    @Override
    protected String generateUniqueId() {
        return "PREDATOR-" + ++idCounter;
    }

    /**
     * Determines the direction of predator's next move.
     * If there is prey within {@link #HUNT_RANGE}, predator moves towards it.
     * Otherwise, it chooses a random direction.
     *
     * @return direction in which predator should move
     */
    @Override
    protected Direction getNextMoveDirection() {
        if (getEnergy() < MAX_ENERGY - HUNT_ENERGY_GAIN) {
            Prey nearestPrey = findNearestPrey();
            if (nearestPrey != null) {
                Event.log(EventType.HUNT, world, this, nearestPrey);
                return getPosition().directionTo(nearestPrey.getPosition());
            }
        }
        return Direction.getRandom();
    }

    /**
     * Finds the nearest prey within predator's hunting range.
     *
     * @return nearest prey or {@code null} if none is within range
     */
    private Prey findNearestPrey() {
        if (getPosition() == null) {
            throw new IllegalStateException("Predator has no position");
        }

        Prey nearestPrey = null;
        int minDistance = Integer.MAX_VALUE;

        for (Animal animal : world.getAnimals()) {
            if (animal instanceof Prey) {
                int distance = getPosition().distanceTo(animal.getPosition());
                if (distance <= HUNT_RANGE && distance < minDistance) {
                    nearestPrey = (Prey) animal;
                    minDistance = distance;
                }
            }
        }

        return nearestPrey;
    }

    /**
     * Allows the predator to eat prey if one is on an adjacent cell.
     * After eating the prey, predator gains energy equal to {@link #HUNT_ENERGY_GAIN}.
     */
    @Override
    protected void eat() {
        Prey prey = findNearestPrey();
        if (prey != null && getPosition().distanceTo(prey.getPosition()) == 1) {
            Event.log(EventType.DIE_EATEN, world, prey, this);
            prey.die();

            Event.log(EventType.EAT_PREY, world, this, prey);
            setEnergy(getEnergy() + HUNT_ENERGY_GAIN);
        }
    }

    /**
     * Creates a new predator as offspring.
     *
     * @param position position for the new predator
     * @return new predator object with initial energy equal to {@link #OFFSPRING_ENERGY}
     */
    @Override
    protected Animal createOffspring(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Offspring position cannot be null");
        }

        return new Predator(world, position);
    }
}