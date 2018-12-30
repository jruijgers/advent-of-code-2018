package org.salandur.advent_of_code;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toList;

public class Day10 {
    private static final Pattern LIGHT_PARSER = Pattern.compile("position=<([- ]?\\d+), ([- ]?\\d+)> velocity=<([- ]?\\d+), ([- ]?\\d+)>");

    public static void main(String[] args) throws IOException {
        List<Light> lights = Files.lines(Main.pathFromClasspath("/day10.txt")).map(Day10::parseLight).collect(toList());

        int elapsedSeconds = 0;
        while (getMessageHeight(lights) > 10) {
            lights.stream().forEach(l -> l.move());
            elapsedSeconds++;
        }

        System.out.println("Day 10.1: the message will read:");
        printLights(lights);
        System.out.println("Day 10.2: the message appeared after " + elapsedSeconds + " seconds");
    }

    private static Light parseLight(String lightString) {
        Matcher matcher = LIGHT_PARSER.matcher(lightString);

        if (matcher.matches()) {
            return new Light(getInt(matcher, 1), getInt(matcher, 2), getInt(matcher, 3), getInt(matcher, 4));
        } else {
            throw new RuntimeException("WTF: " + lightString);
        }
    }

    private static int getMessageHeight(List<Light> lights) {
        int minY = lights.stream().mapToInt(l -> l.y).min().getAsInt();
        int maxY = lights.stream().mapToInt(l -> l.y).max().getAsInt();

        return maxY - minY + 1;
    }

    private static void printLights(List<Light> lights) {
        int minX = lights.stream().mapToInt(l -> l.x).min().getAsInt();
        int maxX = lights.stream().mapToInt(l -> l.x).max().getAsInt();
        int minY = lights.stream().mapToInt(l -> l.y).min().getAsInt();
        int maxY = lights.stream().mapToInt(l -> l.y).max().getAsInt();

        System.out.println(StringUtils.repeat("=", maxX - minX + 1));
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                Optional<Light> light = findLight(lights, x, y);
                System.out.print(light.isPresent() ? "X" : " ");
            }
            System.out.println();
        }
        System.out.println(StringUtils.repeat("=", maxX - minX + 1));
    }

    private static Optional<Light> findLight(List<Light> lights, int x, int y) {
        return lights.stream().filter(l -> l.x == x && l.y == y).findFirst();
    }

    private static Integer getInt(Matcher m, int pos) {
        return Integer.valueOf(m.group(pos).trim());
    }

    private static class Light {
        private int x;
        private int y;

        private int vX;
        private int vY;

        public Light(int x, int y, int vX, int vY) {
            this.x = x;
            this.y = y;
            this.vX = vX;
            this.vY = vY;
        }

        public void move() {
            x += vX;
            y += vY;
        }

        @Override
        public String toString() {
            return String.format("[position=<%d,%d> velocity=<%d,%d>]", x, y, vX, vY);
        }
    }
}
