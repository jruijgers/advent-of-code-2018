package org.salandur.advent_of_code.day15;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.math3.ml.distance.ManhattanDistance;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.salandur.advent_of_code.common.Direction.DIRECTIONS;

public abstract class BaseUnit extends AbstractWorldElement implements Unit {
    public static final ManhattanDistance MANHATTAN_DISTANCE = new ManhattanDistance();

    private static int manhattanDistance(Unit left, Unit right) {
        return (int) MANHATTAN_DISTANCE.compute(left.getCoordinates(), right.getCoordinates());
    }

    private final World world;

    private int health = 200;
    int attackPower = 3;

    public BaseUnit(int x, int y, World world) {
        super(x, y);
        this.world = world;
    }

    @Override
    public void tick() {
        if (isAlive()) {
            // System.out.println("turn " + this);
            move();
            attack();
        }
    }

    @Override
    public void reduceHealth(int attackPower) {
        health = Math.max(0, health - attackPower);
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public boolean isAvailable() {
        return isAlive();
    }

    @Override
    public boolean isEmpty() {
        return !isAlive();
    }

    @Override
    public int getHealth() {
        return health;
    }

    private void move() {
        if (!isNextToEnemy()) {
            List<WorldElement> enemyAvailablePoints = getEnemies().flatMap(this::availableEnemyPoints).filter(WorldElement::isEmpty).sorted().collect(toList());
            if (enemyAvailablePoints.isEmpty()) return;

            Optional<PathFinder.Path> path = new PathFinder(this, enemyAvailablePoints, world).findShortestPath();
            if (path.isPresent()) {
                this.setX(path.get().firstStep().x());
                this.setY(path.get().firstStep().y());
            }
        }
    }

    private boolean isNextToEnemy() {
        return getEnemies().
                mapToInt(enemy -> manhattanDistance(this, enemy)).
                filter(i -> i == 1).
                count() > 0;
    }

    private void attack() {
        List<Unit> enemies = getEnemies().collect(toList());
        Optional<Unit> closestEnemy = closestEnemy(enemies);

        if (closestEnemy.isPresent()) {
            closestEnemy.get().reduceHealth(attackPower);
        }
    }

    private Optional<Unit> closestEnemy(List<Unit> enemies) {
        return enemies.stream().
                filter(enemy -> manhattanDistance(this, enemy) == 1).
                sorted(new BaseUnit.UnitComparator()).findFirst();
    }

    private Stream<Unit> getEnemies() {
        return world.getUnits().stream().filter(u -> isEnemy(u)).filter(Unit::isAlive);
    }


    private Stream<WorldElement> availableEnemyPoints(Unit enemy) {
        return DIRECTIONS.stream().map(d -> world.findElement(enemy.move(d))).filter(WorldElement::isEmpty);
    }

    @Override
    public char getIdentifier() {
        return isAlive() ? getClass().getSimpleName().charAt(0) : ' ';
    }

    @Override
    public String toString() {
        return super.toString() + "(" + health + ")";
    }

    private static class UnitComparator implements Comparator<Unit> {
        @Override
        public int compare(Unit l, Unit r) {
            return new CompareToBuilder().append(l.getHealth(), r.getHealth()).append(((BaseUnit) l).y(), ((BaseUnit) r).y()).append(((BaseUnit) l).x(), ((BaseUnit) r).x()).toComparison();
        }

    }
}
