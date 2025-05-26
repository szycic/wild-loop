package org.wildloop;

public class Predator extends Animal {
    private static final int HUNT_RANGE = 3;
    private static final int HUNT_ENERGY_GAIN = 10;

    public Predator(Position position, int energy, int maxAge) {
        super(position, energy, maxAge);
    }
    public Predator(Predator predator) {
        super(predator);
    }
    public Predator() {
        super();
    }

    @Override
    protected Direction getNextMoveDirection() {
        Prey nearestPrey = findNearestPrey();
        if (nearestPrey != null) {
            return getPosition().directionTo(nearestPrey.getPosition());
        }
        return Direction.getRandom();
    }

    private Prey findNearestPrey() {
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

    @Override
    protected void eat() {
        Prey prey = findNearestPrey();
        if (prey != null && getPosition().distanceTo(prey.getPosition()) == 1) {
            prey.die();
            setEnergy(getEnergy() + HUNT_ENERGY_GAIN);
        }
    }

    @Override
    protected Animal createOffspring(Position position) {
        return new Predator(position, REPRODUCTION_ENERGY_COST, getMaxAge());
    }
}
