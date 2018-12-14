package org.salandur.advent_of_code;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;

public class Day5 {
    public static void main(String[] args) throws IOException {
        Path data = Path.of("day5.txt");

        String basePolymer = Files.readAllLines(data).get(0);

        LinkedList<Character> chars = new LinkedList<>();

        basePolymer.chars().forEach(currentChar -> {
            char previousChar = chars.size() == 0 ? 0 : chars.peek();

            if (currentChar != previousChar && Character.toLowerCase(currentChar) == Character.toLowerCase(previousChar)) {
                // we have the same char, in different case
                chars.pop();
            } else {
                chars.push((char) currentChar);
            }
        });

        System.out.printf("Day 5.1: the polymer contracts to a length of %d\n", chars.size());
    }
}
