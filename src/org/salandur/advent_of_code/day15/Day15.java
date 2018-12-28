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

        // Reddit: https://github.com/ShaneMcC/aoc-2018/tree/master/15/tests
        // run("day15.reddit1.txt", 13400);
        // run("day15.reddit2.txt", 13987);
        // run("day15.reddit3.txt", 10325);
        // run("day15.reddit4.txt", 10804);
        // run("day15.reddit5.txt", 10620);
        // run("day15.reddit6.txt", 16932);
        // run("day15.reddit7.txt", 10234);
        // run("day15.reddit8.txt", 10430);
        // run("day15.reddit9.txt", 12744);
        // run("day15.reddit10.txt", 14740);

        // run("day15.chris.txt", 261855);
        run("day15.txt", 250648);
    }

    private static void run(String dataFile, int expectedScore) throws IOException {
        int elfAttackPower = 3;
        Day15 day15 = new Day15();
        day15.parseDataFile(dataFile, elfAttackPower);

        long numberOfElves = day15.numberOfElves();

        day15.run(numberOfElves, false);

        int remainingHealth = day15.remainingHealth();
        int score = day15.numberOfTurns * remainingHealth;

        System.out.printf("Day 15.1: Total complete turns: %d, remaining health: %d, final score: %d\n", day15.numberOfTurns, remainingHealth, score);

        if (score != expectedScore) {
            System.out.println("  expected score of " + expectedScore + ", got " + score);
        }

        boolean elvesDied;
        elfAttackPower = 4;
        do {
            day15 = new Day15();
            day15.parseDataFile(dataFile, elfAttackPower);
            day15.run(numberOfElves, true);

            // after the battle, get the number of elves remaining
            elvesDied = day15.numberOfElves() < numberOfElves;
            if (elvesDied) elfAttackPower++;
        } while (elvesDied);

        remainingHealth = day15.remainingHealth();
        score = day15.numberOfTurns * remainingHealth;

        System.out.printf("Day 15.2: Total complete turns: %d, remaining health: %d, final score: %d with elf attack power %d\n", day15.numberOfTurns, remainingHealth, score, elfAttackPower);
    }

    private long numberOfElves() {
        return world.getUnits().stream().filter(u -> u instanceof Elf).count();
    }

    private World world = new World();
    private int numberOfTurns;

    private void parseDataFile(String dataFile, int elfAttackPower) throws IOException {
        List<String> dataLines = Files.readAllLines(Path.of(dataFile));
        for (int y = 0; y < dataLines.size(); y++) {
            for (int x = 0; x < dataLines.get(y).length(); x++) {
                world.parseLocation(dataLines.get(y).charAt(x), x, y, elfAttackPower);
            }
        }
    }

    private void run(long numberOfElves, boolean stopWhenElfDies) {
        numberOfTurns = 0;

        while (numberOfEnemyTypes() > 1) {
            if (world.tick()) {
                numberOfTurns++;
            }

            if (stopWhenElfDies && numberOfElves() < numberOfElves) {
                return;
            }
            // showAndStoreWorld(numberOfTurns, remainingHealth());
        }
    }

    private void showAndStoreWorld(int numberOfTurns, int remainingHealth) {
        String status = String.format("Turn: %d, remaining health: %d\n", numberOfTurns, remainingHealth);

        Main.clearTerminal();
        System.out.print(status);
        System.out.println(world);
        System.out.println(world.getUnits());
        System.console().readLine();

        // try {
        //     Files.createDirectories(Path.of("log", "day15"));
        //     Path log = Path.of("log", "day15", "turn" + numberOfTurns + ".txt");
        //     Files.write(log, status.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        //     Files.write(log, world.toString().getBytes(), StandardOpenOption.APPEND);
        //     Files.write(log, StringUtils.join(world.getUnits(), '\n').getBytes(), StandardOpenOption.APPEND);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }

    private int remainingHealth() {
        return world.getUnits().stream().mapToInt(Unit::getHealth).sum();
    }

    private int numberOfEnemyTypes() {
        Map<Character, Long> numberOfEnemyTypes = world.getUnits().stream().collect(groupingBy(WorldElement::getIdentifier, Collectors.counting()));
        return numberOfEnemyTypes.size();
    }
}
