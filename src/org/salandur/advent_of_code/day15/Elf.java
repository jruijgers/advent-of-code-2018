package org.salandur.advent_of_code.day15;

public class Elf extends BaseUnit {
    public Elf(int x, int y, int attackPower, World world) {
        super(x, y, world);
        this.attackPower = attackPower;
    }

    @Override
    public boolean isEnemy(Unit unit) {
        return unit instanceof Goblin;
    }
}
