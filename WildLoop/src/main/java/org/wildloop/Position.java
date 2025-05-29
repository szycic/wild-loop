package org.wildloop;

public record Position(int x, int y) {

    public Direction directionTo(Position target) {
        int dx = Integer.compare(target.x() - this.x, 0);
        int dy = Integer.compare(target.y() - this.y, 0);

        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direction.EAST : Direction.WEST;
        } else {
            return dy > 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }

    public Direction directionFrom(Position target) {
        int dx = Integer.compare(this.x - target.x(), 0);
        int dy = Integer.compare(this.y - target.y(), 0);

        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direction.WEST : Direction.EAST;
        } else {
            return dy > 0 ? Direction.NORTH : Direction.SOUTH;
        }
    }

    public int distanceTo(Position target) {
        return Math.abs(this.x - target.x()) + Math.abs(this.y - target.y());
    }

    public Position move(Direction direction) {
        return new Position(this.x + direction.getDx(), this.y + direction.getDy());
    }
}
