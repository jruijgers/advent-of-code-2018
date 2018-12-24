package org.salandur.advent_of_code.day15;

public class Wall extends AbstractWorldElement {
    public Wall(int x, int y) {
        super(x, y);
    }

    @Override
    public void tick() {
    }

    @Override
    public char getIdentifier() {
        return '#';
    }
}
