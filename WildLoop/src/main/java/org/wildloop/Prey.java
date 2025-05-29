package org.wildloop;

public class Prey extends Animal {
    private static final int FLEE_RANGE = SimulationConfig.getValue("prey.flee.range");
    private static final int GRAZE_ENERGY_GAIN = SimulationConfig.getValue("prey.graze.energy.gain");

    public Prey(Position position, int energy, int maxAge) {
        super(position, energy, maxAge);
    }

    @Override
    protected Direction getNextMoveDirection() {
        Predator nearestPredator = findNearestPredator();
        if (nearestPredator != null) {
            return getPosition().directionFrom(nearestPredator.getPosition());
        }
        return Direction.getRandom();
    }

    private Predator findNearestPredator() {
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

    @Override
    protected void eat() {
        setEnergy(getEnergy() + GRAZE_ENERGY_GAIN);
    }

    @Override
    protected Animal createOffspring(Position position) {
        return new Prey(position, REPRODUCTION_ENERGY_COST, getMaxAge());
    }
}
