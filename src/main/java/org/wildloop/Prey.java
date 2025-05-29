package org.wildloop;

/**
 * Reprezentuje ofiarę w symulacji. Ofiara ucieka przed obiektami klasy Predator
 * w określonym zasięgu i zyskuje energię poprzez pasienie się.
 */
public class Prey extends Animal {
    /** Maksymalny zasięg, w którym ofiara może wykryć drapieżnika */
    private static final int FLEE_RANGE = SimulationConfig.getValue("prey.flee.range");
    /** Ilość energii uzyskiwana przez ofiarę podczas żerowania */
    private static final int GRAZE_ENERGY_GAIN = SimulationConfig.getValue("prey.graze.energy.gain");

    /**
     * Tworzy nową ofiarę.
     *
     * @param position początkowa pozycja ofiary
     * @param energy początkowa energia ofiary
     * @param maxAge maksymalny wiek, który może osiągnąć ofiara
     */
    public Prey(Position position, int energy, int maxAge) {
        super(position, energy, maxAge);
    }

    /**
     * Określa kierunek następnego ruchu ofiary.
     * Jeśli w zasięgu {@link #FLEE_RANGE} znajduje się drapieżnik, ofiara ucieka w przeciwnym kierunku.
     * W przeciwnym razie wybiera losowy kierunek.
     *
     * @return kierunek, w którym ofiara powinna się poruszyć
     */
    @Override
    protected Direction getNextMoveDirection() {
        Predator nearestPredator = findNearestPredator();
        if (nearestPredator != null) {
            return getPosition().directionFrom(nearestPredator.getPosition());
        }
        return Direction.getRandom();
    }

    /**
     * Znajduje najbliższego drapieżnika w zasięgu wykrywania ofiary.
     *
     * @return najbliższy drapieżnik lub {@code null}, jeśli żaden nie znajduje się w zasięgu
     */
    private Predator findNearestPredator() {
        if (world == null) {
            throw new IllegalStateException("Ofiara nie jest przypisana do świata");
        }
        if (getPosition() == null) {
            throw new IllegalStateException("Ofiara nie ma określonej pozycji");
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
     * Pozwala ofierze paść się, zwiększając jej energię o {@link #GRAZE_ENERGY_GAIN}.
     */
    @Override
    protected void eat() {
        if (getEnergy() < MAX_ENERGY - GRAZE_ENERGY_GAIN) {
            setEnergy(getEnergy() + GRAZE_ENERGY_GAIN);
        }
    }

    /**
     * Tworzy nową ofiarę jako potomka.
     *
     * @param position pozycja dla nowej ofiary
     * @return nowy obiekt ofiary z początkową energią równą {@link #OFFSPRING_ENERGY}
     */
    @Override
    protected Animal createOffspring(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Pozycja potomka nie może być pusta");
        }

        return new Prey(position, OFFSPRING_ENERGY, getMaxAge());
    }
}