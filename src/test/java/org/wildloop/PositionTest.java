package org.wildloop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {
    @Test
    void directionTo_ShouldReturnEast_WhenTargetIsToTheRight() {
        Position start = new Position(0, 0);
        Position target = new Position(2, 0);
        assertEquals(Direction.EAST, start.directionTo(target));
    }

    @Test
    void directionTo_ShouldReturnWest_WhenTargetIsToTheLeft() {
        Position start = new Position(2, 2);
        Position target = new Position(0, 2);
        assertEquals(Direction.WEST, start.directionTo(target));
    }

    @Test
    void directionTo_ShouldReturnNorth_WhenTargetIsAbove() {
        Position start = new Position(0, 2);
        Position target = new Position(0, 0);
        assertEquals(Direction.NORTH, start.directionTo(target));
    }

    @Test
    void directionTo_ShouldReturnSouth_WhenTargetIsBelow() {
        Position start = new Position(0, 0);
        Position target = new Position(0, 2);
        assertEquals(Direction.SOUTH, start.directionTo(target));
    }

    @Test
    void distanceTo_ShouldCalculateCorrectDistance() {
        Position start = new Position(0, 0);
        Position target = new Position(3, 4);
        assertEquals(7, start.distanceTo(target));
    }

    @Test
    void newPosition_ShouldCreateCorrectPositionInAllDirections() {
        Position start = new Position(5, 5);

        assertEquals(new Position(5, 4), start.newPosition(Direction.NORTH));
        assertEquals(new Position(6, 5), start.newPosition(Direction.EAST));
        assertEquals(new Position(5, 6), start.newPosition(Direction.SOUTH));
        assertEquals(new Position(4, 5), start.newPosition(Direction.WEST));
    }

}