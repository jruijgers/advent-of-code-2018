package org.salandur.advent_of_code.day15;

public abstract class AbstractWorldElement extends PointImpl implements WorldElement {
    AbstractWorldElement(int x, int y) {
        super(x, y);
    }


    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String toString() {
        return getIdentifier() + super.toString();
    }
}
