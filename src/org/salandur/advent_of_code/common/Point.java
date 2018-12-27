package org.salandur.advent_of_code.common;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Objects;

public class Point implements Comparable<Point> {

    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    protected void setX(int x) {
        this.x = x;
    }

    protected void setY(int y) {
        this.y = y;
    }

    public double[] getCoordinates() {
        return new double[]{x, y};
    }

    public Point move(Point direction) {
        return new Point(x + direction.x(), y + direction.y());
    }

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
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
