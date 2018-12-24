package org.salandur.advent_of_code.day15;

public interface Unit extends WorldElement {
    boolean isEnemy(Unit unit);

    void reduceHealth(int attackPower);

    boolean isAlive();

    int getHealth();
}
