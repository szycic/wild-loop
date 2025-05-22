package org.wildloop;

public class Predator extends Animal {
    public Predator(Position position, int energy, int maxAge) {
        super(position, energy, maxAge);
    }
    public Predator(Predator predator) {
        super(predator);
    }
    public Predator() {
        super();
    }

    public void hunt(Prey prey) {}
}
