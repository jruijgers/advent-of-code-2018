package org.salandur.advent_of_code.day20;

public class Direction implements PathPart {
    private final char indicator;
    private final Path parent;

    public Direction(Path parent, char indicator) {
        this.parent = parent;
        this.indicator = indicator;
    }


    @Override
    public int getPathLength() {
        return 1;
    }

    @Override
    public int getLongestPathLength() {
        return 1;
    }

    @Override
    public String getPathLengths() {
        return "1";
    }

    @Override
    public String toString() {
        return "" + indicator;
    }
}
