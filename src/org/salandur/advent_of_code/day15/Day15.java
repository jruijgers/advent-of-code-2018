package org.salandur.advent_of_code.day15;

import org.apache.commons.lang3.StringUtils;
import org.salandur.advent_of_code.Main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class Day15 {
    public static void main(String[] args) throws IOException {
        // run("day15.example1.txt", 27730);
        // run("day15.example2.txt", 36334);
        // run("day15.example3.txt", 39514);
        // run("day15.example4.txt", 27755);
        // run("day15.example5.txt", 28944);
        // run("day15.example6.txt", 18740);
        // run("day15.chris.txt", 261855);
        run("day15.txt", 231264); // not correct ,but don't know the correct score...
    }

    private static void run(String dataFile, int expectedScore) throws IOException {
        Day15 day15 = new Day15();
        day15.parseDataFile(dataFile);

        int score = day15.run();
        if (score != expectedScore) {
            System.out.println("  expected score of " + expectedScore + ", got " + score);
        }
    }

    private World world = new World();

    private void parseDataFile(String dataFile) throws IOException {
        List<String> dataLines = Files.readAllLines(Path.of(dataFile));
        for (int y = 0; y < dataLines.size(); y++) {
            for (int x = 0; x < dataLines.get(y).length(); x++) {
                world.parseLocation(dataLines.get(y).charAt(x), x, y);
            }
        }
    }

    private int run() {
        int numberOfTurns = 0;

        while (numberOfEnemyTypes() > 1) {
            if (world.tick()) {
                numberOfTurns++;
            }
        }

        int remainingHealth = remainingHealth();

        int score = numberOfTurns * remainingHealth;
        System.out.printf("Day 15.1: Total complete turns: %d, remaining health: %d, final score: %d\n", numberOfTurns, remainingHealth, score);
        return score;
    }

    private void showAndStoreWorld(int numberOfTurns, int remainingHealth) {
        String status = String.format("Turn: %d, remaining health: %d\n", numberOfTurns, remainingHealth);

        Main.clearTerminal();
        System.out.print(status);
        System.out.println(world);

        try {
            Files.createDirectories(Path.of("log", "day15"));
            Path log = Path.of("log", "day15", "turn" + numberOfTurns + ".txt");
            Files.write(log, status.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Files.write(log, world.toString().getBytes(), StandardOpenOption.APPEND);
            Files.write(log, StringUtils.join(world.getUnits(), '\n').getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int remainingHealth() {
        return world.getUnits().stream().mapToInt(Unit::getHealth).sum();
    }

    private int numberOfEnemyTypes() {
        Map<Character, Long> numberOfEnemyTypes = world.getUnits().stream().collect(groupingBy(WorldElement::getIdentifier, Collectors.counting()));
        return numberOfEnemyTypes.size();
    }
}
