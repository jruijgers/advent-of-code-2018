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

    public static final int WAIT = 250;

    public static void main(String[] args) throws IOException {
        run("day15.txt");
    }

    private static void run(String dataFile) throws IOException {
        Day15 day15 = new Day15();
        day15.parseDataFile(dataFile);

        day15.run();
    }

    private void run() throws IOException {
        int numberOfTurns = 0;
        showAndStoreWorld(numberOfTurns, remainingHealth());

        while (numberOfEnemyTypes() > 1) {
            Instant start = Instant.now();
            if (world.tick()) {
                numberOfTurns++;
            }
            Duration duration = Duration.between(start, Instant.now());

            showAndStoreWorld(numberOfTurns, remainingHealth());

            if (duration.toMillis() < WAIT) {
                sleep(WAIT - duration.toMillis());
            }
        }

        int remainingHealth = remainingHealth();
        showAndStoreWorld(numberOfTurns, remainingHealth);

        // sum the remaining health
        System.out.printf("Total turns: %d, remaining health: %d, final score: %d\n", numberOfTurns-1, remainingHealth, (numberOfTurns - 1) * remainingHealth);
        System.out.printf("Total turns: %d, remaining health: %d, final score: %d\n", numberOfTurns, remainingHealth, (numberOfTurns) * remainingHealth);
        System.out.printf("Total turns: %d, remaining health: %d, final score: %d\n", numberOfTurns+1, remainingHealth, (numberOfTurns+1) * remainingHealth);
    }

    private void sleep(long i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
        }
    }

    private void showAndStoreWorld(int numberOfTurns, int remainingHealth) throws IOException {
        String status = String.format("Turn: %d, remaining health: %d\n", numberOfTurns, remainingHealth);

        Main.clearTerminal();
        System.out.print(status);
        System.out.println(world);

        Files.createDirectories(Path.of("log", "day15"));
        Path log = Path.of("log", "day15", "turn" + numberOfTurns + ".txt");
        Files.write(log, status.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Files.write(log, world.toString().getBytes(), StandardOpenOption.APPEND);
        Files.write(log, StringUtils.join(world.getUnits(), '\n').getBytes(), StandardOpenOption.APPEND);
    }

    private int remainingHealth() {
        return world.getUnits().stream().mapToInt(Unit::getHealth).sum();
    }

    private int numberOfEnemyTypes() {
        Map<Character, Long> numberOfEnemyTypes = world.getUnits().stream().collect(groupingBy(WorldElement::getIdentifier, Collectors.counting()));
        return numberOfEnemyTypes.size();
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

}
