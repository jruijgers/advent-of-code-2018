package org.salandur.advent_of_code;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day01 {
    public static void main(String[] args) throws IOException {
        Path day1 = Main.pathFromClasspath("/day01.txt");

        System.out.printf("Day 1.1: Resulting frequency: %d\n", findResultingFrequency(day1));

        System.out.printf("Day 2.2: First repeated frequency: %d\n", findFirstRepeatingFrequency(day1));
    }

    private static int findResultingFrequency(Path day1) throws IOException {
        return Files.
                lines(day1).
                parallel().
                mapToInt(Integer::parseInt).
                sum();
    }

    private static Integer findFirstRepeatingFrequency(Path adjustments) throws IOException {
        Set<Integer> allFrequencies = new HashSet<>();

        int currentFrequency = 0;

        allFrequencies.add(currentFrequency);

        List<String> adjustmentsList = Files.readAllLines(adjustments);

        while (true) {
            for (String s : adjustmentsList) {
                int frequencyAdjustment = Integer.parseInt(s);
                // System.out.printf("current frequency %d, change of %d, resulting in %d\n", frequency, frequencyAdjustment, frequency + frequencyAdjustment);
                currentFrequency += frequencyAdjustment;
                if (allFrequencies.contains(currentFrequency)) return currentFrequency;
                allFrequencies.add(currentFrequency);
            }
        }
    }
}
