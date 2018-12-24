package org.salandur.advent_of_code.day15;

public interface Point extends Comparable<Point> {
    double[] getCoordinates();
    Point move(Point base) ;
    int x();
    int y();

    boolean isOn(Point other);
}
