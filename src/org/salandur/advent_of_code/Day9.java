package org.salandur.advent_of_code;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day9 {

    public static void main(String[] args) throws IOException {
        List<String> data = Files.readAllLines(Path.of("day9.txt"));

        data.forEach(Day9::runGames);
    }

    private static void runGames(String description) {
        Matcher matcher = Pattern.compile("(\\d+) players; last marble is worth (\\d+) points").matcher(description);

        if (matcher.find()) {
            Day9 game = new Day9(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2)));
            runGame( game);

            Day9 game2 = new Day9(game.numberOfPlayers, game.numberOfMarbles * 100);
            runGame( game2);
        }
    }

    private static void runGame( Day9 game) {
        game.runGame();
        System.out.printf("Day 9: the highest score for %d players and %d marbles is: %d\n", game.numberOfPlayers, game.numberOfMarbles, game.highScore());
    }

    private final int numberOfPlayers;
    private final int numberOfMarbles;
    private Map<Integer, Long> playerScores = new HashMap<>();

    private Day9(int numberOfPlayers, int numberOfMarbles) {
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfMarbles = numberOfMarbles;
    }

    private Long highScore() {
        return playerScores.values().stream().mapToLong(Long::longValue).max().getAsLong();
    }

    private void runGame() {
        CircleLinkedList currentPosition = new CircleLinkedList(0);
        currentPosition.left = currentPosition.right = currentPosition;

        for (int currentMarble = 1; currentMarble <= numberOfMarbles; currentMarble++) {
            if (currentMarble % 23 == 0) {
                currentPosition = currentPosition.moveLeft(7);

                int currentPlayer = (currentMarble - 1) % numberOfPlayers;
                Long currentPlayerScore = playerScores.getOrDefault(currentPlayer, 0l);
                currentPlayerScore += currentMarble + currentPosition.marbleValue;
                playerScores.put(currentPlayer, currentPlayerScore);

                currentPosition = currentPosition.remove();
            } else {
                currentPosition = new CircleLinkedList(currentMarble).insertAt(currentPosition.moveRight(2));
            }
        }
    }

    private static class CircleLinkedList {
        private final int marbleValue;
        private CircleLinkedList left;
        private CircleLinkedList right;

        public CircleLinkedList(int value) {
            this.marbleValue = value;
            left = this;
            right = this;
        }

        public CircleLinkedList insertAt(CircleLinkedList currentPosition) {
            left = currentPosition.left;
            right = currentPosition;
            left.right = this;
            right.left = this;

            return this;
        }

        public CircleLinkedList remove() {
            left.right = right;
            right.left = left;
            return right;
        }

        public CircleLinkedList moveLeft(int positions) {
            return positions == 0 ? this : left.moveLeft(positions - 1);
        }

        public CircleLinkedList moveRight(int positions) {
            return positions == 0 ? this : right.moveRight(positions - 1);
        }

        private String formattedValue() {
            return String.format("%3d", marbleValue);
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            if (left != this) {
                b.append(left.formattedValue()).append(" < ");
            }
            b.append(formattedValue());
            if (right != this) {
                b.append(" > ").append(right.formattedValue());
            }
            return b.toString();
        }
    }
}
