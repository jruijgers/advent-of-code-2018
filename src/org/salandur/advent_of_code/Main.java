package org.salandur.advent_of_code;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException {
        Path day1 = Path.of("day1.txt");
        int resultingFrequency = Files.
                lines(day1).
                parallel().
                mapToInt(Integer::parseInt).
                sum();
        System.out.printf("1: Resulting frequency: %d\n", resultingFrequency);

        System.out.printf("2: First repeated frequency: %d\n", findFirstRepeatingFrequency(day1));
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
