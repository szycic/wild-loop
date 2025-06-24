package org.wildloop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {
    @Test
    void getDx_ShouldReturnCorrectValues() {
        assertEquals(0, Direction.NORTH.getDx());
        assertEquals(1, Direction.EAST.getDx());
        assertEquals(0, Direction.SOUTH.getDx());
        assertEquals(-1, Direction.WEST.getDx());
    }

    @Test
    void getDy_ShouldReturnCorrectValues() {
        assertEquals(-1, Direction.NORTH.getDy());
        assertEquals(0, Direction.EAST.getDy());
        assertEquals(1, Direction.SOUTH.getDy());
        assertEquals(0, Direction.WEST.getDy());
    }

    @Test
    void getRandom_ShouldReturnValidDirection() {
        Direction randomDir = Direction.getRandom();
        assertNotNull(randomDir);
        assertTrue(randomDir == Direction.NORTH ||
                randomDir == Direction.SOUTH ||
                randomDir == Direction.EAST ||
                randomDir == Direction.WEST);
    }
}
