package org.wildloop;

/**
 * Reprezentuje kierunki świata i związane z nimi wektory przesunięcia.
 * Każdy kierunek ma przypisane składowe dx i dy określające przesunięcie 
 * na płaszczyźnie 2D przy ruchu w tym kierunku.
 *
 * @see Position
 */
public enum Direction {
    /** Kierunek północny (góra), wektor (0,-1) */
    NORTH(0, -1),
    /** Kierunek wschodni (prawo), wektor (1,0) */
    EAST(1, 0),
    /** Kierunek południowy (dół), wektor (0,1) */
    SOUTH(0, 1),
    /** Kierunek zachodni (lewo), wektor (-1,0) */
    WEST(-1, 0);

    /** Składowa x wektora przesunięcia */
    private final int dx;
    /** Składowa y wektora przesunięcia */
    private final int dy;

    /**
     * Tworzy kierunek o zadanych składowych wektora przesunięcia.
     *
     * @param dx przesunięcie w osi x
     * @param dy przesunięcie w osi y
     */
    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /** @return składowa x wektora przesunięcia dla tego kierunku */
    public int getDx() {
        return dx;
    }

    /** @return składowa y wektora przesunięcia dla tego kierunku */
    public int getDy() {
        return dy;
    }

    /**
     * Zwraca losowo wybrany kierunek spośród wszystkich możliwych.
     * 
     * @return losowy kierunek
     */
    public static Direction getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}