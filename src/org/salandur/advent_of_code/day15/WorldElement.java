package org.salandur.advent_of_code.day15;

public interface WorldElement extends Point {
    void tick();

    double[] getCoordinates();

    char getIdentifier();

    boolean isEmpty();

    boolean isAvailable();
}
