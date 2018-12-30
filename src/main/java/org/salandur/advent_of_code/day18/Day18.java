package org.salandur.advent_of_code.day18;

import org.salandur.advent_of_code.Main;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Day18 {
    private static final int ITERATIONS_PER_DOT = 5000;

    private static final Character OPEN_GROUND = '.';
    private static final Character TREES = '|';
    private static final Character LUMBERYARD = '#';

    public static void main(String[] args) throws IOException {
        new Day18().
                parseFile("src/main/resources/day18.txt").
                run(10).
                showResults();

        new Day18().parseFile("src/main/resources/day18.txt").
                run(1000000000).
                showResults();
    }

    private Character[] map;
    private int width;
    private int height;
    private int iterations;

    private Day18 run(int numberOfIterations) {
        // we have a repeating pattern. In my case 1,000 iterations gives the same result as 1,000,000,000 iterations
        while (iterations++ < Math.min(1000, numberOfIterations)) {
            map = createNextIteration();
        }

        return this;
    }

    private Character[] createNextIteration() {
        Character[] nextIteration = Arrays.copyOf(map, map.length);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int location = toLocation(x, y);
                if (map[location] == OPEN_GROUND && countAdjacent(x, y, TREES) >= 3) {
                    nextIteration[location] = TREES;
                } else if (map[location] == TREES && countAdjacent(x, y, LUMBERYARD) >= 3) {
                    nextIteration[location] = LUMBERYARD;
                } else if (map[location] == LUMBERYARD && !(countAdjacent(x, y, LUMBERYARD) >= 1 && countAdjacent(x, y, TREES) >= 1)) {
                    nextIteration[location] = OPEN_GROUND;
                }
            }
        }

        return nextIteration;
    }

    private int toLocation(int x, int y) {
        return x + y * width;
    }

    private int countAdjacent(int x, int y, Character type) {
        int count = 0;
        count += getType(y - 1, x - 1) == type ? 1 : 0;
        count += getType(y - 1, x) == type ? 1 : 0;
        count += getType(y - 1, x + 1) == type ? 1 : 0;
        count += getType(y, x - 1) == type ? 1 : 0;
        count += getType(y, x + 1) == type ? 1 : 0;
        count += getType(y + 1, x - 1) == type ? 1 : 0;
        count += getType(y + 1, x) == type ? 1 : 0;
        count += getType(y + 1, x + 1) == type ? 1 : 0;
        return count;
    }

    private int getType(int y, int x) {
        if (y < 0 || y >= height || x < 0 || x >= width) {
            return ' ';
        }

        return map[toLocation(x, y)];
    }

    private Day18 showResults() {
        long numberOfTrees = countType(TREES);
        long numberOfLumberyards = countType(LUMBERYARD);

        System.out.printf("Day 18: after %d iterations: trees=%d, lumberyards=%d, resource value = %d\n", iterations - 1, numberOfTrees, numberOfLumberyards, numberOfLumberyards * numberOfTrees);

        return this;
    }

    private long countType(Character type) {
        return Arrays.stream(map).filter(c -> c == type).count();
    }

    private Day18 parseFile(String input) throws IOException {
        List<List<Character>> map = new ArrayList<>();
        for (String line : Files.readAllLines(Main.pathFromClasspath(input))) {
            List<Character> row = new ArrayList<>();

            for (char c : line.toCharArray()) {
                row.add(c);
            }

            map.add(row);
        }

        this.height = map.size();
        this.width = map.get(0).size();
        this.map = map.stream().flatMap(List::stream).collect(toList()).toArray(new Character[0]);

        return this;
    }

    private void printMap() {
        Main.clearTerminal();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(map[toLocation(x, y)]);
            }
            System.out.println();
        }
        System.out.println();
    }
}
