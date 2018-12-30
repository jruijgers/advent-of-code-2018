package org.salandur.advent_of_code;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.OptionalInt;
import java.util.Stack;
import java.util.stream.IntStream;

public class Day05 {
    public static void main(String[] args) throws IOException {
        Path data = Main.pathFromClasspath("/day05.txt");

        String basePolymer = Files.readAllLines(data).get(0);

        int numberOfChars = contractPolymer(basePolymer, 0);

        System.out.printf("Day 5.1: the polymer contracts to a length of %d\n", numberOfChars);

        OptionalInt smallestCollapsedPolymer = IntStream.rangeClosed('a', 'z').map(c -> contractPolymer(basePolymer, c)).min();

        System.out.printf("Day 5.2: the smallest polymer contracts to a length of %d\n", smallestCollapsedPolymer.getAsInt());
    }

    private static int contractPolymer(String basePolymer, int skipChar) {
        Stack<Character> chars = new Stack<>();
        basePolymer.chars().forEach(currentChar -> {
            char previousChar = chars.size() == 0 ? 0 : chars.peek();

            if (currentChar != previousChar && Character.toLowerCase(currentChar) == Character.toLowerCase(previousChar)) {
                // we have the same char, in different case, so remove it
                chars.pop();
            } else if (Character.toLowerCase(currentChar) != Character.toLowerCase(skipChar)) {
                chars.push((char) currentChar);
            }
        });

        return chars.size();
    }
}
