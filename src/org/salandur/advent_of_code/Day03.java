package org.salandur.advent_of_code;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day03 {
    private static final Pattern CLAIM_PATTERN = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");
    private static final Integer FABRIC_WIDTH = 1000;
    private static final Integer FABRIC_HEIGHT = 1000;

    public static void main(String[] args) throws IOException {
        List<String> day3input = Files.readAllLines(Path.of("day03.txt"));

        // discover amount of fabric claimed multiple times
        List<FabricClaim> fabricClaims = day3input.stream().map(Day03::parseFabricClaim).collect(Collectors.toList());
        HashMap<Integer, List<FabricClaim>> claimedFabric = fabricClaims.stream().collect(HashMap::new, Day03::claimFabric, (a, b) -> {});

        if (ArrayUtils.contains(args, "print-fabric-claims")) {
            printFabric(claimedFabric);
        }

        System.out.printf("Day 3.1: number of fabric squares claimed multiple times: %d\n", claimedFabric.entrySet().stream().filter(e -> e.getValue().size() > 1).count());

        // find the claim(s) without overlaps
        Stream<FabricClaim> claimsWithoutOverlap = fabricClaims.stream().filter(fc -> fc.overlappingFabrics.size() == 0);

        claimsWithoutOverlap.forEach(fc -> System.out.println("Day 3.2: claim without overlap: " + fc));
    }

    private static void printFabric(HashMap<Integer, List<FabricClaim>> claimedFabric) {
        System.out.print('\u2554');
        System.out.print(StringUtils.repeat('\u2550', FABRIC_WIDTH));
        System.out.println('\u2557');

        for (int y = 0; y < FABRIC_HEIGHT; y++) {
            System.out.print('\u2551');
            for (int x = 0; x < FABRIC_WIDTH; x++) {
                int size = claimedFabric.getOrDefault(y * FABRIC_WIDTH + x, Collections.emptyList()).size();
                System.out.print(size == 0 ? " " : (size == 1 ? "\u00B7" : "X"));
            }
            System.out.println('\u2551');
        }

        System.out.print('\u255A');
        System.out.print(StringUtils.repeat('\u2550', FABRIC_WIDTH));
        System.out.println('\u255D');
    }

    private static FabricClaim parseFabricClaim(String input) {
        // format is '#1 @ 1,3: 4x4'
        Matcher matcher = CLAIM_PATTERN.matcher(input);
        if (matcher.matches()) {
            return new FabricClaim(matcher.group(1), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)));
        } else {
            throw new RuntimeException("invalid input! " + input);
        }
    }

    private static void claimFabric(Map<Integer, List<FabricClaim>> fabric, FabricClaim fabricClaim) {
        for (int y = 0; y < fabricClaim.height; y++) {
            for (int x = 0; x < fabricClaim.width; x++) {
                int key = FABRIC_WIDTH * (fabricClaim.top + y) + (fabricClaim.left + x);
                List<FabricClaim> list = fabric.getOrDefault(key, new ArrayList());
                list.add(fabricClaim);
                fabric.put(key, list);

                list.stream().forEach(fabricClaim::addOverlappingFabric);
            }
        }
    }

    private static class FabricClaim {
        public final String claimId;
        public final int left;
        public final int top;
        public final int width;
        public final int height;

        private Set<FabricClaim> overlappingFabrics = new HashSet<>();

        private FabricClaim(String claimId, int left, int top, int width, int height) {
            this.claimId = claimId;
            this.left = left;
            this.top = top;
            this.width = width;
            this.height = height;
        }

        public void addOverlappingFabric(FabricClaim other) {
            if (other != this) {
                overlappingFabrics.add(other);
                other.overlappingFabrics.add(this);
            }
        }

        public String toString() {
            return String.format("#%s @ %s,%s: %sx%s", claimId, left, top, width, height);
        }
    }
}
