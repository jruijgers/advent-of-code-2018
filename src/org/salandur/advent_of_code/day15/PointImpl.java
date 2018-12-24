package org.salandur.advent_of_code.day15;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.List;
import java.util.Objects;

public class PointImpl implements Point, Comparable<Point> {

    int x;
    int y;

    public PointImpl(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }

    @Override
    public double[] getCoordinates() {
        return new double[]{x, y};
    }

    @Override
    public Point move(Point direction) {
        PointImpl moved = new PointImpl(x + direction.x(), y + direction.y());
        return moved;
    }

    @Override
    public boolean isOn(Point other) {
        // System.out.println("comparing "+ this + " to " + other);
        return this.x == other.x() && this.y == other.y();
    }

    @Override
    public int compareTo(Point other) {
        return new CompareToBuilder().append(this.y, other.y()).append(this.x, other.x()).toComparison();
    }

    @Override
    public String toString() {
        return String.format("[%dx%d]", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PointImpl)) return false;
        PointImpl point = (PointImpl) o;
        return x == point.x &&
                y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
