package org.wildloop;

public class Prey extends Animal {
    public Prey(Position position, int energy, int maxAge) {
        super(position, energy, maxAge);
    }
    public Prey(Prey prey) {
        super(prey);
    }
    public Prey() {
        super();
    }

    public void flee(Predator predator) {}
}
