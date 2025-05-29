package org.wildloop;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }

    public Direction directionTo(Position target) {
        int dx = Integer.compare(target.getX() - this.x, 0);
        int dy = Integer.compare(target.getY() - this.y, 0);

        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direction.EAST : Direction.WEST;
        } else {
            return dy > 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }
    public Direction directionFrom(Position target) {
        int dx = Integer.compare(this.x - target.getX(), 0);
        int dy = Integer.compare(this.y - target.getY(), 0);

        if (Math.abs(dx) > Math.abs(dy)) {
            return dx > 0 ? Direction.WEST : Direction.EAST;
        } else {
            return dy > 0 ? Direction.NORTH : Direction.SOUTH;
        }
    }
    public int distanceTo(Position target) {
        return Math.abs(this.x - target.getX()) + Math.abs(this.y - target.getY());
    }
    public Position move(Direction direction) {
        return new Position(this.x + direction.getDx(), this.y + direction.getDy());
    }
}
