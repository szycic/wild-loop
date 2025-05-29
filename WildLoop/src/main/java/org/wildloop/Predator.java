package org.wildloop;

/**
 * Reprezentuje drapieżnika w symulacji. Drapieżnik poluje na obiekty klasy Prey
 * w określonym zasięgu i zyskuje energię poprzez ich zjadanie.
 */
public class Predator extends Animal {
    /** Maksymalny zasięg, w którym drapieżnik może wykryć ofiarę */
    private static final int HUNT_RANGE = SimulationConfig.getValue("predator.hunt.range");
    /** Ilość energii uzyskiwana przez drapieżnika po zjedzeniu ofiary */
    private static final int HUNT_ENERGY_GAIN = SimulationConfig.getValue("predator.hunt.energy.gain");

    /**
     * Tworzy nowego drapieżnika.
     *
     * @param position początkowa pozycja drapieżnika
     * @param energy początkowa energia drapieżnika
     * @param maxAge maksymalny wiek, który może osiągnąć drapieżnik
     */
    public Predator(Position position, int energy, int maxAge) {
        super(position, energy, maxAge);
    }

    /**
     * Określa kierunek następnego ruchu drapieżnika.
     * Jeśli w zasięgu {@link #HUNT_RANGE} znajduje się ofiara, drapieżnik porusza się w jej kierunku.
     * W przeciwnym razie wybiera losowy kierunek.
     *
     * @return kierunek, w którym drapieżnik powinien się poruszyć
     */
    @Override
    protected Direction getNextMoveDirection() {
        Prey nearestPrey = findNearestPrey();
        if (nearestPrey != null) {
            return getPosition().directionTo(nearestPrey.getPosition());
        }
        return Direction.getRandom();
    }

    /**
     * Znajduje najbliższą ofiarę w zasięgu polowania drapieżnika.
     *
     * @return najbliższa ofiara lub {@code null}, jeśli żadna nie znajduje się w zasięgu
     */
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

    /**
     * Pozwala drapieżnikowi zjeść ofiarę, jeśli jakaś znajduje się na sąsiednim polu.
     * Po zjedzeniu ofiary drapieżnik zyskuje energię równą {@link #HUNT_ENERGY_GAIN}.
     */
    @Override
    protected void eat() {
        Prey prey = findNearestPrey();
        if (prey != null && getPosition().distanceTo(prey.getPosition()) == 1) {
            prey.die();
            setEnergy(getEnergy() + HUNT_ENERGY_GAIN);
        }
    }

    /**
     * Tworzy nowego drapieżnika jako potomka.
     *
     * @param position pozycja dla nowego drapieżnika
     * @return nowy obiekt drapieżnika z początkową energią równą {@link #REPRODUCTION_ENERGY_COST}
     */
    @Override
    protected Animal createOffspring(Position position) {
        return new Predator(position, REPRODUCTION_ENERGY_COST, getMaxAge());
    }
}