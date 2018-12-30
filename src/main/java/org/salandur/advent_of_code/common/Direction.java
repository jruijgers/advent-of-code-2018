package org.salandur.advent_of_code.common;

import java.util.List;

public class Direction extends Point {
    public static final Direction LEFT = new Direction(-1, 0);
    public static final Direction RIGHT = new Direction(1, 0);
    public static final Direction UP = new Direction(0, -1);
    public static final Direction DOWN = new Direction(0, 1);

    public static final List<Direction> DIRECTIONS = List.of(LEFT, RIGHT, UP, DOWN);

    public Direction(int x, int y) {
        super(x, y);
    }
}
