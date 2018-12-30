package org.salandur.advent_of_code.day15;

public class Goblin extends BaseUnit {
    public Goblin(int x, int y, World world) {
        super(x, y, world);
    }

    @Override
    public boolean isEnemy(Unit unit) {
        return unit instanceof Elf;
    }
}
