package org.salandur.advent_of_code.day15;

import org.salandur.advent_of_code.common.Point;

public interface Unit extends WorldElement {
    boolean isEnemy(Unit unit);

    void reduceHealth(int attackPower);

    boolean isAlive();

    int getHealth();

    Point move(Point direction);
}
