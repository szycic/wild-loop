package org.wildloop;

/**
 * Abstrakcyjna klasa bazowa reprezentująca zwierzę w symulacji.
 * Definiuje podstawowe zachowania i atrybuty wspólne dla wszystkich zwierząt,
 * takie jak poruszanie się, reprodukcja, jedzenie i zarządzanie energią.
 */
public abstract class Animal {
    /** Koszt energetyczny pojedynczego ruchu */
    protected static final int MOVE_ENERGY_COST = SimulationConfig.getValue("animal.move.energy.cost");
    /** Próg energii wymagany do reprodukcji */
    protected static final int REPRODUCTION_ENERGY_THRESHOLD = SimulationConfig.getValue("animal.reproduction.energy.threshold");
    /** Koszt energetyczny reprodukcji */
    protected static final int REPRODUCTION_ENERGY_COST = SimulationConfig.getValue("animal.reproduction.energy.cost");

    /** Aktualna pozycja zwierzęcia w świecie */
    private Position position;
    /** Aktualny poziom energii zwierzęcia */
    private int energy;
    /** Aktualny wiek zwierzęcia */
    private int age;
    /** Maksymalny wiek, po którym zwierzę umiera */
    private final int maxAge;
    /** Referencja do świata, w którym żyje zwierzę */
    protected World world;

    /**
     * Tworzy nowe zwierzę o zadanych parametrach początkowych.
     *
     * @param position początkowa pozycja
     * @param energy początkowy poziom energii
     * @param maxAge maksymalny wiek zwierzęcia
     */
    public Animal(Position position, int energy, int maxAge) {
        if (position == null) {
            throw new IllegalArgumentException("Pozycja nie może być pusta");
        }
        if (energy < 0) {
            throw new IllegalArgumentException("Energia początkowa nie może być ujemna");
        }
        if (maxAge <= 0) {
            throw new IllegalArgumentException("Maksymalny wiek musi być większy od zera");
        }

        this.position = position;
        this.energy = energy;
        this.maxAge = maxAge;
        this.age = 0;
    }
    
    // Gettery
    /** @return aktualna pozycja zwierzęcia */
    public Position getPosition() {
        return position;
    }
    /** @return aktualny poziom energii */
    public int getEnergy() {
        return energy;
    }
    /** @return aktualny wiek */
    public int getAge() {
        return age;
    }
    /** @return maksymalny możliwy wiek */
    public int getMaxAge() {
        return maxAge;
    }

    // Settery
    /** @param position nowa pozycja zwierzęcia */
    public void setPosition(Position position) {
        this.position = position;
    }
    /** @param energy nowy poziom energii */
    public void setEnergy(int energy) {
        this.energy = energy;
    }
    /** Zwiększa wiek zwierzęcia o 1 */
    public void incrementAge() {
        this.age++;
    }
    /** @param world świat, w którym zostanie umieszczone zwierzę */
    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Przesuwa zwierzę w kierunku określonym przez {@link #getNextMoveDirection()}.
     * Ruch jest wykonywany, tylko jeśli nowa pozycja jest prawidłowa i pusta.
     * Każdy ruch kosztuje energię określoną przez {@link #MOVE_ENERGY_COST}.
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
                energy -= MOVE_ENERGY_COST;
            }
        }
    }

    /**
     * Określa kierunek następnego ruchu zwierzęcia.
     * Implementacja zależy od konkretnego typu zwierzęcia.
     * 
     * @return kierunek ruchu lub {@code null}, jeśli zwierzę nie może się poruszyć
     */
    protected abstract Direction getNextMoveDirection();

    /**
     * Próbuje rozmnożyć zwierzę, jeśli ma wystarczająco energii.
     * Potomstwo jest tworzone na sąsiednim pustym polu.
     */
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

    /**
     * Tworzy nowe zwierzę tego samego typu.
     * Implementacja zależy od konkretnego typu zwierzęcia.
     *
     * @param position pozycja dla nowego zwierzęcia
     * @return nowe zwierzę
     */
    protected abstract Animal createOffspring(Position position);

    /**
     * Szuka pustego pola sąsiadującego z aktualną pozycją zwierzęcia.
     *
     * @return pozycję pustego sąsiedniego pola lub {@code null}, jeśli nie znaleziono
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
     * Definiuje zachowanie żywieniowe zwierzęcia.
     * Implementacja zależy od konkretnego typu zwierzęcia.
     */
    protected abstract void eat();

    /**
     * Usuwa zwierzę ze świata (zwierzę umiera).
     */
    public void die() {
        world.removeAnimal(this);
    }

    /**
     * Aktualizuje stan zwierzęcia w każdej turze symulacji.
     * Wykonuje następujące czynności:
     * <ol>
     * <li>Zwiększa wiek</li>
     * <li>Wykonuje ruch</li>
     * <li>Próbuje znaleźć pożywienie</li>
     * <li>Próbuje się rozmnożyć, jeśli ma wystarczająco energii</li>
     * <li>Umiera, jeśli skończyła się energia lub przekroczył maksymalny wiek</li>
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
        }

        if (energy <= 0 || age >= maxAge) {
            die();
        }
    }
}