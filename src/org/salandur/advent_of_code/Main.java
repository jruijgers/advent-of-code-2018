package org.salandur.advent_of_code;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        int resultingFrequency = Files.
                lines(Path.of("day1.txt")).
                parallel().
                mapToInt(Integer::parseInt).
                sum();
        System.out.printf("1: Resulting frequency: %d", resultingFrequency);
    }
}
