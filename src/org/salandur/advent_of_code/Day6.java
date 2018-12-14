package org.salandur.advent_of_code;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.math3.ml.distance.ManhattanDistance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class Day6 {

    public static final ManhattanDistance MANHATTAN_DISTANCE = new ManhattanDistance();

    public static void main(String[] args) throws IOException {
        Path data = Path.of("day6.txt");

        List<SpacePoint> spacePoints = Files.lines(data).filter(s -> s.length() > 0).map(Day6::parseDataPoint).collect(toList());
        int width = spacePoints.stream().mapToInt(p -> p.x).max().getAsInt();
        int height = spacePoints.stream().mapToInt(p -> p.y).max().getAsInt();

        List<Point> points = generateAllPoints(width, height).collect(toList());
        points.forEach(p -> manhattanDistance(p, spacePoints));

        SpacePoint bestSpacePoint = spacePoints.stream().filter(SpacePoint::isFinite).reduce(Day6::selectLargestSurface).get();

        System.out.printf("Day 6.1: the largest area of %d is around %dx%d\n", bestSpacePoint.associatedPoints.size(), bestSpacePoint.x, bestSpacePoint.y);

        List<Integer> collect = points.stream().map(p -> sumOfDistances(p, spacePoints)).filter(i -> i < 10000).collect(toList());
        System.out.printf("Day 6.2: the area with total distances less than 10,000 is %d\n", collect.size());
    }

    private static SpacePoint selectLargestSurface(SpacePoint sp1, SpacePoint sp2) {
        return sp1.associatedPoints.size() > sp2.associatedPoints.size() ? sp1 : sp2;
    }

    private static SpacePoint parseDataPoint(String s) {
        return new SpacePoint(s.split(","));
    }

    private static Stream<Point> generateAllPoints(int width, int height) {
        return IntStream.rangeClosed(0, height)
                .mapToObj(y -> IntStream.rangeClosed(0, width)
                        .mapToObj(x -> new Point(x, y, x == 0 || x == width || y == 0 || y == height)))
                .flatMap(Function.identity());
    }

    private static void manhattanDistance(Point point, List<SpacePoint> spacePoint) {
        Map<SpacePoint, Integer> distances = spacePoint.stream().collect(toMap(Function.identity(), sp -> manhattanDistance(point, sp)));

        int minDistance = findMinDistance(distances);

        List<SpacePoint> closestSpacePoints = distances.entrySet().stream().filter(e -> e.getValue() == minDistance).map(Map.Entry::getKey).collect(toList());

        if (closestSpacePoints.size() == 1) {
            closestSpacePoints.get(0).addPoint(point);
        }
    }

    private static int manhattanDistance(Point point, SpacePoint spacePoint) {
        return (int) MANHATTAN_DISTANCE.compute(point.coordinates(), spacePoint.coordinates());
    }

    private static int findMinDistance(Map<SpacePoint, Integer> distances) {
        return distances.entrySet().stream().min((l, r) -> l.getValue() - r.getValue()).get().getValue();
    }

    private static Integer sumOfDistances(Point point, List<SpacePoint> spacePoints) {
        return spacePoints.stream().mapToInt(s -> manhattanDistance(point, s)).sum();
    }


    private static class Point {
        protected final int x;
        protected final int y;
        protected final boolean onEdge;

        private Point(int x, int y, boolean onEdge) {
            this.x = x;
            this.y = y;
            this.onEdge = onEdge;
        }

        public double[] coordinates() {
            return new double[]{(double) x, (double) y};
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                    .append("x", x)
                    .append("y", y)
                    .toString();
        }
    }

    private static class SpacePoint extends Point {

        private boolean isInfinite = false;
        private final List<Point> associatedPoints = new ArrayList<>();

        public SpacePoint(String[] points) {
            super(Integer.parseInt(points[0].trim()), Integer.parseInt(points[1].trim()), false);
        }

        public void addPoint(Point point) {
            associatedPoints.add(point);

            isInfinite |= point.onEdge;
        }

        public boolean isFinite() {
            return !isInfinite;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                    .append("x", x)
                    .append("y", y)
                    .append("isInfinite", isInfinite)
                    .append("associatedPoints", associatedPoints.size())
                    .toString();
        }
    }
}
