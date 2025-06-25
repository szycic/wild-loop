package org.wildloop;

/**
 * Represents an immutable position in two-dimensional space.
 * Implemented as a record for better conciseness.
 *
 * @param x horizontal coordinate of the position
 * @param y vertical coordinate of the position
 * @see Direction
 */
public record Position(int x, int y) {

    /**
     * Determines the main direction to the specified target position.
     * Chooses EAST/WEST if the x difference is greater,
     * or NORTH/SOUTH if the y difference is greater or equal.
     *
     * @param target target position
     * @return direction leading the shortest path to the target
     */
    public Direction directionTo(Position target) {
        int dx = target.x() - this.x;
        int dy = target.y() - this.y;

        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direction.EAST : Direction.WEST;
        } else {
            return dy > 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }

    /**
     * Determines the main direction from the specified source position.
     * Works similarly to {@link #directionTo(Position)}, but with reversed directions.
     *
     * @param target source position
     * @return direction leading from the source to the current position
     */
    public Direction directionFrom(Position target) {
        int dx = target.x() - this.x;
        int dy = target.y() - this.y;

        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direction.WEST : Direction.EAST;
        } else {
            return dy > 0 ? Direction.NORTH : Direction.SOUTH;
        }
    }

    /**
     * Calculates the distance to the target position.
     * It is the sum of absolute differences of x and y coordinates.
     *
     * @param target target position
     * @return |x1-x2| + |y1-y2|
     */
    public int distanceTo(Position target) {
        return Math.abs(this.x - target.x()) + Math.abs(this.y - target.y());
    }

    /**
     * Creates a new position moved one step in the given direction.
     * Due to record immutability, returns a new object instead of
     * modifying the existing one.
     *
     * @param direction movement direction
     * @return new position after making the move
     */
    public Position newPosition(Direction direction) {
        return new Position(this.x + direction.getDx(), this.y + direction.getDy());
    }

    /**
     * Returns a string representation of the position in the format "(x, y)".
     *
     * @return string representation of the position
     */
    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}