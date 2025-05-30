package org.wildloop;

/**
 * Represents world directions and associated displacement vectors.
 * Each direction has assigned dx and dy components defining the displacement
 * on a 2D plane when moving in that direction.
 *
 * @see Position
 */
public enum Direction {
    /** Northern direction (up), vector (0,-1) */
    NORTH(0, -1),
    /** Eastern direction (right), vector (1,0) */
    EAST(1, 0),
    /** Southern direction (down), vector (0,1) */
    SOUTH(0, 1),
    /** Western direction (left), vector (-1,0) */
    WEST(-1, 0);

    /** X component of displacement vector */
    private final int dx;
    /** Y component of displacement vector */
    private final int dy;

    /**
     * Creates a direction with given displacement vector components.
     *
     * @param dx x-axis displacement
     * @param dy y-axis displacement
     */
    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /** @return x component of displacement vector for this direction */
    public int getDx() {
        return dx;
    }

    /** @return y component of displacement vector for this direction */
    public int getDy() {
        return dy;
    }

    /**
     * Returns a randomly selected direction from all available directions.
     *
     * @return random direction
     */
    public static Direction getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}