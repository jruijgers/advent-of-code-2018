package org.salandur.advent_of_code.day15;

public class EmptySpace extends AbstractWorldElement {
    EmptySpace(int x, int y) {
        super(x, y);
    }

    @Override
    public void tick() {
    }

    @Override
    public char getIdentifier() {
        return '\u00B7';
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}
