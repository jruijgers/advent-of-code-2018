package org.salandur.advent_of_code;

import org.salandur.advent_of_code.day12.Day12;
import org.salandur.advent_of_code.day13.Day13;
import org.salandur.advent_of_code.day15.Day15;
import org.salandur.advent_of_code.day18.Day18;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Day01.main(args);
        Day02.main(args);
        Day03.main(args);
        Day04.main(args);
        Day05.main(args);
        Day06.main(args);
        Day07.main(args);
        Day08.main(args);
        Day09.main(args);
        Day10.main(args);
        Day11.main(args);
        Day12.main(args);
        Day13.main(args);
        Day14.main(args);
        Day15.main(args);
        Day16.main(args);
        Day17.main(args);
        Day18.main(args);
        Day19.main(args);
    }

    public static void clearTerminal() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // eat it
        }
    }
}
