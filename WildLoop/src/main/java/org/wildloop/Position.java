package org.wildloop;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Position(Position pos) {
        this(pos.x, pos.y);
    }
    public Position() {
        this(0, 0);
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

    public int distanceTo(Position other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }
}
