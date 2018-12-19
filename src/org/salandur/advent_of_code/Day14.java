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
        Day14 day14 = new Day14(2, Arrays.asList(3, 7));

        day14.run(9);
        day14.run(5);
        day14.run(18);
        day14.run(2018);
        day14.run(47801);

        // day14 = new Day14(2, Arrays.asList(3, 7));
        day14.run("51589");
        day14.run("01245");
        day14.run("92510");
        day14.run("59414");
        day14.run("047801");
    }

    private final int[] elfRecipe;
    private final List<Integer> recipes;

    public Day14(int numberOfElves, List<Integer> initalRecipies) {
        elfRecipe = IntStream.range(0, numberOfElves).toArray();
        recipes = new ArrayList<>(initalRecipies);
    }

    private void run(int numberOfRecipes) {
        while (recipes.size() < numberOfRecipes + 10) {
            iterate();
        }

        System.out.printf("Day 14.1: after %d recipes, the last 10 scores are: %s\n", numberOfRecipes, StringUtils.join(recipes.subList(numberOfRecipes, 10 + numberOfRecipes), ""));
    }

    private void run(String sequence) {
        while(recipes.size() < 250000 && scoreString().indexOf(sequence) < 0) {
            iterate();
        }

        System.out.printf("Day 14.2: the score sequence '%s' appears for the first time after %d recipes (%d total)\n", sequence, scoreString().indexOf(sequence), recipes.size());
    }

    private String scoreString() {
        return StringUtils.join(recipes, "");
    }

    private void printRecipes() {
        for (int i = 0; i < recipes.size(); i++) {
            int elf = ArrayUtils.indexOf(elfRecipe, i);

            if (elf == 0) {
                System.out.printf("(%d)", recipes.get(i));
            } else if (elf == 1) {
                System.out.printf("[%d]", recipes.get(i));
            } else {
                System.out.printf(" %d ", recipes.get(i));
            }
        }
        System.out.println();
    }

    private void iterate() {
        // 1. calculate new recipe score
        int recipeScore = calculateScore();

        // 2. add score to recipes -> 10+ gives 2 recipes
        recipes.addAll(Integer.toString(recipeScore).chars().map(v -> Integer.valueOf("" + (char) v)).boxed().collect(Collectors.toList()));

        // 3. select the next recipe for each elf
        for (int e = 0; e < elfRecipe.length; e++) {
            elfRecipe[e] = (elfRecipe[e] + 1 + recipes.get(elfRecipe[e])) % recipes.size();
        }
    }

    private int calculateScore() {
        int score = 0;
        for (int e = 0; e < elfRecipe.length; e++) {
            score += recipes.get(elfRecipe[e]);
        }
        return score;
    }
}
