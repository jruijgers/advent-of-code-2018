package org.salandur.advent_of_code;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day14 {
    public static void main(String[] args) {
        Day14 day14 = new Day14(2, "37");

        day14.run(9);
        day14.run(5);
        day14.run(18);
        day14.run(2018);
        day14.run(47801);

        day14.run("51589");
        day14.run("01245");
        day14.run("92510");
        day14.run("59414");
        day14.run("047801");
    }

    private final int[] elfRecipe;
    private final StringBuilder recipes;

    public Day14(int numberOfElves, String initialRecipes) {
        elfRecipe = IntStream.range(0, numberOfElves).toArray();
        recipes = new StringBuilder(initialRecipes);

        Runtime.getRuntime().addShutdownHook(shutdownHook());
    }

    private Thread shutdownHook() {
        return new Thread(() -> {
            System.out.println("\nNumber of recipes: " + recipes.length());
        });
    }

    private void run(int numberOfRecipes) {
        while (recipes.length() < numberOfRecipes + 10) {
            iterate();
        }

        System.out.printf("Day 14.1: after %d recipes, the last 10 scores are: %s\n", numberOfRecipes, recipes.substring(numberOfRecipes, 10 + numberOfRecipes), "");
    }

    private void run(String sequence) {
        int loops = 0;
        while (recipes.length() < 1000000 && scoreString().indexOf(sequence) < 0) {
            iterate();

            if (loops++ % 1000 == 0) System.out.print(".");
        }

        System.out.printf("Day 14.2: the score sequence '%s' appears for the first time after %d recipes (%d total)\n", sequence, scoreString().indexOf(sequence), recipes.length());
    }

    private String scoreString() {
        return StringUtils.join(recipes, "");
    }

    private void printRecipes() {
        for (int i = 0; i < recipes.length(); i++) {
            int elf = ArrayUtils.indexOf(elfRecipe, i);

            if (elf == 0) {
                System.out.printf("(%d)", recipes.charAt(i));
            } else if (elf == 1) {
                System.out.printf("[%d]", recipes.charAt(i));
            } else {
                System.out.printf(" %d ", recipes.charAt(i));
            }
        }
        System.out.println();
    }

    private void iterate() {
        // 1. calculate new recipe score
        int recipeScore = calculateScore();

        // 2. add score to recipes -> 10+ gives 2 recipes
        recipes.append(recipeScore);

        // 3. select the next recipe for each elf
        for (int e = 0; e < elfRecipe.length; e++) {
            elfRecipe[e] = (elfRecipe[e] + 1 + getScore(elfRecipe[e])) % recipes.length();
        }
    }

    private int calculateScore() {
        int score = 0;
        for (int r : elfRecipe) {
            score += getScore(r);
        }
        return score;
    }

    private int getScore(int position) {
        return Integer.valueOf("" + recipes.charAt(position));
    }
}
