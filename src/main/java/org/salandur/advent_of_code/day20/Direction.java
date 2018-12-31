package org.salandur.advent_of_code.day20;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Direction implements PathElement {
    private static final Map<Character, Character> OPPOSITE_DIRECTION = Map.of(
            'S', 'N',
            'N', 'S',
            'E', 'W',
            'W', 'E'
    );

    public static boolean canAddDirection(String head, Direction current) {
        return head.length() == 0 || OPPOSITE_DIRECTION.get(head.charAt(head.length() - 1)) != current.indicator;
    }

    private final char indicator;
    private final Path parent;

    public Direction(Path parent, char indicator) {
        this.parent = parent;
        this.indicator = indicator;
    }


    @Override
    public int getPathLength() {
        return 1;
    }

    @Override
    public List<String> getPathStrings(String head, List<PathElement> tail) {
        List<String> ps = new ArrayList<>();

        if (canAddDirection(head, this)) {
            head = head + indicator;
            ps.add(head);

            if (!tail.isEmpty()) {
                PathElement firstElement = tail.remove(0);
                ps.addAll(firstElement.getPathStrings(head, tail));
            }
        }

        return ps;
    }

    @Override
    public int getLongestPathLength() {
        return 1;
    }

    @Override
    public String getPathLengths() {
        return "1";
    }

    @Override
    public String toString() {
        return "" + indicator;
    }
}
