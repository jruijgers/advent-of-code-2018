package org.salandur.advent_of_code.day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Day13 {

    public static void main(String[] args) throws IOException {
        new Day13("src/main/resources/day13.txt").run();
    }

    private String dataFile;
    private List<List<Character>> tracks;
    private List<Cart> carts;

    public Day13(String dataFile) {
        this.dataFile = dataFile;
    }

    public void run() throws IOException {
        parseDataFile();

        while (carts.size() > 1) {
            for (int i = 0; i < carts.size(); i++) {
                moveCart(carts.get(i));
                markCollisions(carts.get(i));
            }

            carts = carts.stream().filter(c -> c.isNotCrashed()).sorted().collect(toList());
        }

        System.out.printf("Day 13: remaining cart at location %d,%d\n", carts.get(0).getX(), carts.get(0).getY());
        // printTracksAndCarts();
    }

    private void moveCart(Cart cart) {
        cart.move();
        cart.turnIfNeeded(tracks.get(cart.getY()).get(cart.getX()));
    }

    private void markCollisions(Cart cart) {
        for (Cart other : carts) {
            if (cart != other && cart.isNotCrashed() && cart.collidesWith(other)) {
                System.out.printf("Day 13: detected crash at location %d,%d\n", cart.getX(), cart.getY());
                cart.setIsCrashed();
                other.setIsCrashed();
            }
        }
    }

    private void parseDataFile() throws IOException {
        LinkedList<List<Character>> tracks = new LinkedList<>();
        this.carts = new ArrayList<>();

        List<String> data = Files.readAllLines(Path.of(dataFile));
        for (String s : data) {
            tracks.add(new ArrayList<>());

            for (char c : s.toCharArray()) {
                if (Cart.CART_DIRECTIONS.contains(c)) {
                    if (c == '<' || c == '>') {
                        tracks.getLast().add('-');
                    } else {
                        tracks.getLast().add('|');
                    }
                    carts.add(new Cart(c, tracks.getLast().size() - 1, tracks.size() - 1));
                } else {
                    tracks.getLast().add(c);
                }
            }
        }

        this.tracks = tracks;
    }

    private void printTracksAndCarts() {
        for (int y = 0; y < tracks.size(); y++) {
            for (int x = 0; x < tracks.get(y).size(); x++) {
                if (!printCart(x, y)) {
                    System.out.print(tracks.get(y).get(x));
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private boolean printCart(int x, int y) {
        List<Cart> cart = carts.stream().filter(c -> c.isOn(x, y)).collect(toList());

        if (cart.size() == 1) {
            System.out.print(cart.get(0).getDirection());
        } else if (cart.size() > 1) {
            System.out.print("X");
        }

        return cart.size() > 0;
    }
}