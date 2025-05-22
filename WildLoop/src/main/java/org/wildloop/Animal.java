package org.wildloop;

public class Animal {
    private Position position;
    private int energy;
    private int age;
    private int maxAge;

    public Animal(Position position, int energy, int maxAge) {
        this.position = position;
        this.energy = energy;
        this.maxAge = maxAge;
        this.age = 0;
    }
    public Animal(Animal animal) {
        this(animal.position, animal.energy, animal.maxAge);
    }
    public Animal() {
        this(new Position(), 100, 10);
    }
    
    public Position getPosition() {
        return position;
    }
    public int getEnergy() {
        return energy;
    }
    public int getAge() {
        return age;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
    public void setEnergy(int energy) {
        this.energy = energy;
    }
    public void incrementAge() {
        this.age++;
    }

    void move() {}
    void eat() {}
    void reproduce() {}
    void die() {}
}
