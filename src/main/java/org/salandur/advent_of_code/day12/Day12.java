package org.salandur.advent_of_code.day12;

import org.salandur.advent_of_code.Main;

import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class Day12 {
    public static void main(String[] args) throws IOException {
        List<String> data = Files.readAllLines(Main.pathFromClasspath("/day12.txt"));

        List<Pot> pots = parsePots(data.remove(0));

        List<Rule> rules = parseRules(data);

        runForGenerations(20, pots, rules);

        // process 50,000,000,000 generations
        // runForGenerations(1000, pots, rules);
        // runForGenerations(2000, pots, rules);
        // runForGenerations(3000, pots, rules);
        // runForGenerations(4000, pots, rules);
        // runForGenerations(5000, pots, rules);

        // this calculation is specific for me
        long generations = 50000000000l;
        long numberOfPots = 86;
        long base = 349;

        printDayResult(generations, numberOfPots, base + numberOfPots * generations);
    }

    private static void runForGenerations(long totalGenerations, List<Pot> pots, List<Rule> rules) {
        for (long generation = 1; generation <= totalGenerations; generation++) {
            pots = nextGeneration(pots, rules);
        }

        printDayResult(totalGenerations, getCount(pots), getSum(pots));
    }

    private static void printDayResult(long totalGenerations, long numberOfPots, long sumOfIndices) {
        System.out.printf("Day 12: after %,d generations, the amount of pots with plants is %d, sum is %d\n", totalGenerations, numberOfPots, sumOfIndices);
    }


    private static long getSum(List<Pot> pots) {
        return pots.stream().filter(p -> p.hasPlant).mapToLong(p -> p.index).sum();
    }

    private static long getCount(List<Pot> pots) {
        return pots.stream().filter(p -> p.hasPlant).count();
    }

    private static List<Pot> nextGeneration(List<Pot> pots, List<Rule> rules) {
        LinkedList<Pot> nextGeneration = new LinkedList<>();
        for (Pot pot : pots) {
            Optional<Pot> lastPot = Optional.ofNullable(nextGeneration.peekLast());
            nextGeneration.add(pot.nextGeneration(rules, lastPot));
        }

        // special cases for the beginning and end of the list
        Pot leftNextGeneration = pots.get(0).left().nextGeneration(rules, Optional.empty());
        if (leftNextGeneration.hasPlant) {
            leftNextGeneration.setRight(nextGeneration.getFirst());
            nextGeneration.add(0, leftNextGeneration);
        }
        Pot rightNextGeneration = pots.get(pots.size() - 1).right().nextGeneration(rules, Optional.empty());
        if (rightNextGeneration.hasPlant) {
            rightNextGeneration.setLeft(nextGeneration.getLast());
            nextGeneration.add(rightNextGeneration);
        }

        return nextGeneration;
    }

    private static List<Pot> parsePots(String initialState) {
        LinkedList<Pot> pots = new LinkedList<>();
        initialState.substring("initial state: ".length()).chars().
                forEach(c -> {
                    Optional<Pot> lastPot = Optional.ofNullable(pots.peekLast());
                    pots.add(new Pot('#' == c, pots.size(), lastPot));
                });

        return pots;
    }

    private static List<Rule> parseRules(List<String> data) {
        return data.stream().
                filter(Predicate.not(String::isEmpty)).
                map(Rule::from).
                collect(toList());
    }

    private static void printPots(List<Pot> pots) {
        boolean printing = pots.get(0).hasPlant;
        for (Pot pot : pots) {
            if (printing)
                System.out.print(pot);
            else
                printing = pot.hasPlant;
        }
        System.out.println(" " + pots.stream().filter(p -> p.hasPlant).findFirst().get().index);
    }
}
