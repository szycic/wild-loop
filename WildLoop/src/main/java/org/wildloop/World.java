package org.wildloop;

import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje świat symulacji, w którym zwierzęta mogą się poruszać i wchodzić w interakcje.
 * Świat jest zorganizowany jako dwuwymiarowa siatka, gdzie każda komórka może zawierać jedno zwierzę.
 */
public class World {
    /** Dwuwymiarowa siatka reprezentująca rozmieszczenie zwierząt */
    private final Animal[][] grid;
    
    /** Lista wszystkich aktywnych zwierząt w świecie */
    private final List<Animal> animals;
    
    /** Licznik wykonanych tur symulacji */
    private int turns;
    
    /**
     * Tworzy nowy świat o określonych wymiarach.
     * 
     * @param width szerokość świata (liczba komórek)
     * @param height wysokość świata (liczba komórek)
     */
    public World(int width, int height) {
        this.grid = new Animal[width][height];
        this.animals = new ArrayList<>();
        this.turns = 0;
    }
    
    /** @return aktualna siatka świata ze zwierzętami */
    public Animal[][] getGrid() {
        return grid;
    }

    /** @return lista wszystkich żywych zwierząt */
    public List<Animal> getAnimals() {
        return animals;
    }

    /** @return liczba wykonanych tur */
    public int getTurns() {
        return turns;
    }

    /** @return szerokość świata */
    public int getWidth() {
        return grid.length;
    }

    /** @return wysokość świata */
    public int getHeight() {
        return grid[0].length;
    }
    
    /**
     * Dodaje nowe zwierzę do świata.
     * 
     * @param animal zwierzę do dodania
     * @throws IllegalArgumentException jeśli zwierzę jest null lub jego pozycja jest nieprawidłowa
     * @throws IllegalStateException jeśli docelowa komórka jest już zajęta
     */
    public void addAnimal(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Cannot add null animal");
        }

        Position position = animal.getPosition();
        if (!isValidPosition(position)) {
            throw new IllegalArgumentException("Invalid position");
        }
        if (!isCellEmpty(position)) {
            throw new IllegalStateException("Cell is not empty");
        }

        grid[position.x()][position.y()] = animal;
        animals.add(animal);
        animal.setWorld(this);
    }

    /**
     * Usuwa zwierzę ze świata.
     * 
     * @param animal zwierzę do usunięcia
     * @throws IllegalArgumentException jeśli zwierzę jest null
     * @throws IllegalStateException jeśli zwierzę nie istnieje w świecie lub jego pozycja jest nieprawidłowa
     */
    public void removeAnimal(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Cannot remove null animal");
        }

        Position position = animal.getPosition();
        if (!animals.contains(animal)) {
            throw new IllegalStateException("Animal is not in the world");
        }
        if (!isValidPosition(position) || grid[position.x()][position.y()] != animal) {
            throw new IllegalStateException("Animal position is invalid or grid position mismatch");
        }

        grid[position.x()][position.y()] = null;
        animals.remove(animal);
        animal.setWorld(null);
        animal.setPosition(null);
        animal.setEnergy(-1);
    }

    /**
     * Sprawdza, czy dana pozycja mieści się w granicach świata.
     * 
     * @param position pozycja do sprawdzenia
     * @return true jeśli pozycja jest prawidłowa, false w przeciwnym razie
     */
    public boolean isValidPosition(Position position) {
        return position.x() >= 0 && position.x() < getWidth()
                && position.y() >= 0 && position.y() < getHeight();
    }

    /**
     * Sprawdza, czy komórka na danej pozycji jest pusta.
     * 
     * @param position pozycja do sprawdzenia
     * @return true jeśli komórka jest pusta, false w przeciwnym razie
     */
    public boolean isCellEmpty(Position position) {
        return isValidPosition(position) && grid[position.x()][position.y()] == null;
    }

    /**
     * Wykonuje jedną turę symulacji, aktualizując stan wszystkich zwierząt
     * i zwiększając licznik tur.
     */
    public void tick() {
        List<Animal> currentAnimals = new ArrayList<>(animals);
        for (Animal animal : currentAnimals) {
            animal.update();
        }
        turns++;
    }

    /**
     * Resetuje licznik tur do zera.
     */
    public void resetTurns() {
        this.turns = 0;
    }
}