package org.salandur.advent_of_code;

import java.util.*;

public class Day9 {

    public static void main(String[] args) {

        // Day9 game = new Day9(9, 25); // should score 32
        // Day9 game = new Day9(10, 1618); // should score 8317
        // Day9 game = new Day9(13, 7999); // should score 146373
        Day9 game = new Day9(452, 70784);
        runGame(1, game);

        Day9 game2 = new Day9(game.numberOfPlayers, game.numberOfMarbles * 100);
        runGame(2, game2);
    }

    private static void runGame(int exercise, Day9 game) {
        game.runGame();
        System.out.printf("Day 9.%d: the highest score for %,d players and %,d marbles is: %,d\n", exercise, game.numberOfPlayers, game.numberOfMarbles, game.highScore());
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

                currentPosition.left.right = currentPosition.right;
                currentPosition.right.left = currentPosition.left;
                currentPosition = currentPosition.right;
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
