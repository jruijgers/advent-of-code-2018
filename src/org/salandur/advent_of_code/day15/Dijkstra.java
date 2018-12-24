package org.salandur.advent_of_code.day15;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.salandur.advent_of_code.day15.Direction.DIRECTIONS;

public class Dijkstra {
    private final Unit unit;
    private final List<Point> enemies;
    private final World world;

    public Dijkstra(Unit unit, List<Point> enemies, World world) {
        this.unit = unit;
        this.enemies = enemies;
        this.world = world;
    }

    public Optional<Path> findShortestPath() {
        Path startPath = new Path();

        Map<Point, Path> visitedPoints = new HashMap<>();
        visitedPoints.put(unit, startPath);

        Queue<Path> paths = new LinkedList<>();

        DIRECTIONS.stream().map(d -> unit.move(d)).filter(p -> world.findElement(p).isEmpty()).forEach(p -> {
            Path newPath = startPath.with(p);
            visitedPoints.put(p, newPath);
            paths.add(newPath);
        });

        Path shortestPath = null;

        while (paths.size() > 0) {
            Path path = paths.remove();

            if (isOnEnemy(path.lastStep())) {
                if (path.isBetterThan(shortestPath)) {
                    shortestPath = path;
                }
            } else if (path.isShortThan(shortestPath)) {
                List<Point> newPoints = DIRECTIONS.stream().
                        map(d -> path.lastStep().move(d)).
                        filter(Predicate.not(p -> path.steps.contains(p))).
                        filter(p -> world.findElement(p).isEmpty()).
                        filter(p -> notVisitedOrSimilar(p, path, visitedPoints)).
                        collect(Collectors.toList());
                for (Point p : newPoints) {
                    Path newPath = path.with(p);
                    visitedPoints.put(p, newPath);
                    paths.add(newPath);
                }
            }
        }

        return Optional.ofNullable(shortestPath);
    }

    private boolean isOnEnemy(Point lastStep) {
        return enemies.contains(lastStep);
    }

    private boolean notVisitedOrSimilar(Point point, Path path, Map<Point, Path> visitedPoints) {
        Path visitedPath = visitedPoints.get(point);
        int newNumberOfSteps = path.numberOfSteps() + 1;

        if (visitedPath == null || newNumberOfSteps < visitedPath.numberOfSteps()) {
            return true;
        } else {
            return newNumberOfSteps == visitedPath.numberOfSteps() && path.firstStep().compareTo(visitedPath.firstStep()) < 0;
        }
    }

    public static class Path implements Comparable<Path> {
        private LinkedList<Point> steps = new LinkedList<>();

        private Path() {
        }

        public Path with(Point p) {
            Path newPath = new Path();
            newPath.steps.addAll(this.steps);
            newPath.steps.add(p);
            return newPath;
        }

        public Point firstStep() {
            return steps.getFirst();
        }

        public Point lastStep() {
            return steps.getLast();
        }

        public boolean isShortThan(Path other) {
            if (other == null) {
                return true;
            } else {
                return this.steps.size() < other.steps.size();
            }
        }

        public boolean isBetterThan(Path other) {
            if (isShortThan(other)) {
                return true;
            } else if (this.steps.size() == other.steps.size()) {
                return firstStep().compareTo(other.firstStep()) < 0;
            } else {
                return false;
            }
        }

        public int numberOfSteps() {
            return steps.size();
        }

        @Override
        public String toString() {
            return "Path[#" + steps.size() + "]";
        }

        public String toFullString() {
            return "Path[#" + steps.size() + "," + steps + "]";
        }

        @Override
        public int compareTo(Path other) {
            return new CompareToBuilder().
                    append(this.numberOfSteps(), other.numberOfSteps()).
                    append(this.firstStep(), other.firstStep()).
                    toComparison();
        }
    }
}
