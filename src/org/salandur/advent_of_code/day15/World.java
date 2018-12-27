package org.salandur.advent_of_code.day15;

import org.salandur.advent_of_code.common.Point;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class World {
    private List<AbstractWorldElement> elements = new ArrayList<>();

    private int width = 0;
    private int height = 0;

    public boolean tick() {
        for (Unit u : getUnits()) {
            if (noMoreEnemies(u)) {
                return false;
            }
            u.tick();
        }
        return true;
    }

    private boolean noMoreEnemies(Unit u) {
        return getUnits().stream().filter(e -> e.isEnemy(u)).count() == 0;
    }

    protected WorldElement findElement(Point p) {
        return elements.stream().filter(e -> e.equals(p)).filter(AbstractWorldElement::isAvailable).findAny().orElse(new EmptySpace(p.x(), p.y()));
    }

    public void parseLocation(char identifier, int x, int y) {
        width = Math.max(width, x + 1);
        height = Math.max(height, y + 1);

        if ('#' == identifier) {
            elements.add(new Wall(x, y));
        } else {
            parseUnit(identifier, x, y);
        }
    }

    private void parseUnit(char identifier, int x, int y) {
        if ('E' == identifier) {
            elements.add(new Elf(x, y, this));
        } else if ('G' == identifier) {
            elements.add(new Goblin(x, y, this));
        }
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                b.append(findElement(new Point(x, y)).getIdentifier());
            }
            b.append('\n');
        }
        return b.toString();
    }

    public List<Unit> getUnits() {
        return elements.stream().filter(e -> e instanceof Unit).map(e -> (Unit) e).filter(Unit::isAlive).sorted().collect(toList());
    }
}