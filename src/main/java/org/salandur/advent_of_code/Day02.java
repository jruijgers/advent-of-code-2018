package org.salandur.advent_of_code;

import org.apache.commons.text.similarity.LevenshteinDetailedDistance;
import org.apache.commons.text.similarity.LevenshteinResults;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class Day02 {
    public static void main(String[] args) throws IOException {
        Path day2 = Main.pathFromClasspath("/day02.txt");

        List<Map<Character, Integer>> result = Files.lines(day2).map(Day02::characterCounters).collect(toList());
        long containsLettersTwice = containsLettersTwice(result);
        long containsLettersThreeTimes = containsLettersThreeTimes(result);

        System.out.println("Day 2.1: calculated checksum: " + containsLettersThreeTimes * containsLettersTwice);

        List<String> boxesWithFabrics = findBoxesWithFabrics(Files.readAllLines(day2));
        System.out.println("Day 2.2: letters of boxes with fabrics: " + getMatchingLetters(boxesWithFabrics.get(0), boxesWithFabrics.get(1)));
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

    private static List<String> findBoxesWithFabrics(List<String> boxes) {
        for (int i = 1; i < boxes.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (containFabric(boxes.get(i), boxes.get(j))) {
                    return List.of(boxes.get(i), boxes.get(j));
                }
            }
        }
        return Collections.emptyList();
    }

    private static boolean containFabric(String box1, String box2) {
        LevenshteinResults result = LevenshteinDetailedDistance.getDefaultInstance().apply(box1, box2);

        return result.getSubstituteCount() == 1 && result.getDeleteCount() == 0 && result.getInsertCount() == 0;
    }

    private static String getMatchingLetters(String box1, String box2) {
        StringBuilder b = new StringBuilder(box1.length());

        for (int i = 0; i < box1.length(); i++) {
            if (box1.charAt(i) == box2.charAt(i)) {
                b.append(box1.charAt(i));
            }
        }
        return b.toString();
    }
}

