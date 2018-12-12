package org.salandur.advent_of_code;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class Day2 {
    public static void main(String[] args) throws IOException {
        Path day2 = Path.of("day2.txt");

        List<Map<Character, Integer>> result = Files.lines(day2).map(Day2::characterCounters).collect(toList());
        long containsLettersTwice = containsLettersTwice(result);
        long containsLettersThreeTimes = containsLettersThreeTimes(result);

        System.out.println("Day 2.1: " + containsLettersThreeTimes * containsLettersTwice);
    }

    private static Map<Character, Integer> characterCounters(String string) {
        return string.chars().collect(HashMap::new,
                (m, c) -> m.merge((char) c, 1, (a, b) -> a + b),
                (m1, m2) -> {
                });
    }

    private static long containsLettersTwice(List<Map<Character, Integer>> result) {
        return result.stream().filter(v -> v.values().contains(2)).count();
    }

    private static long containsLettersThreeTimes(List<Map<Character, Integer>> result) {
        return result.stream().filter(v -> v.values().contains(3)).count();
    }
}

