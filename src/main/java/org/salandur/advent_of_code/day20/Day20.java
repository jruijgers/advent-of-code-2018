package org.salandur.advent_of_code.day20;

import org.apache.commons.lang3.StringUtils;
import org.salandur.advent_of_code.Main;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Day20 {
    public static void main(String[] args) throws IOException {
        Day20 day20 = new Day20();

        // day20.parseFile("/day20.example1.txt");
        // day20.part1(18);
        // day20.part2(10);

        // day20.parseFile("/day20.example2.txt");
        // day20.part1(23);
        // day20.part2(10);

        // day20.parseFile("/day20.example3.txt");
        // day20.part1(31);
        // day20.part2(10);

        day20.parseFile("/day20.txt");
        day20.part1(3755);
        day20.part2(1000);
    }

    private Path path = new Path();
    private List<String> allPaths;

    private void part1(int expectedLength) {
        allPaths = path.getPathStrings();

        int longestPath = allPaths.parallelStream().mapToInt(String::length).max().getAsInt();
        System.out.printf("Day 20.1: the longest path is %d", longestPath);
        if (expectedLength > 0 && longestPath != expectedLength) {
            System.out.printf(" (expected %d!)", expectedLength);
        }
        System.out.println();
    }

    private void part2(int minLength) {
        long pathsWithMinLength = allPaths.parallelStream().filter(s -> s.length() >= minLength).count();
        System.out.printf("Day 20.2: there are %d paths with a minimum length of %d\n", pathsWithMinLength, minLength);
    }

    private void parseFile(String dataFile) throws IOException {
        List<String> dataLines = Files.readAllLines(Main.pathFromClasspath(dataFile));

        path = new Path();
        Path currentPath = null;
        Group currentGroup = null;

        for (String s : dataLines) {
            for (char c : s.toCharArray()) {
                if (c == '^') {
                    currentPath = path;
                } else if (StringUtils.contains("NSEW", c)) {
                    currentPath.add(c);
                } else if (c == '(') {
                    currentGroup = currentPath.newGroup();
                    currentPath = currentGroup.newPath();
                } else if (c == '|') {
                    currentPath = currentGroup.newPath();
                } else if (c == ')') {
                    currentPath = currentGroup.getParent();
                    currentGroup = currentPath.getParent();
                } else if (c == '$') {
                    currentPath = null;
                } else {
                    System.out.println("Unknown input: " + c);
                }
            }
        }
        if (currentGroup != null || currentPath != null || !dataLines.get(0).equals(path.toString())) {
            throw new RuntimeException("parsing didn't work, not finished in a good state");
        }

        // System.out.println(path);
    }
}
