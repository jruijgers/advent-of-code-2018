package org.salandur.advent_of_code.day20;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Path {
    private final Group parent;
    private final LinkedList<PathElement> directions = new LinkedList<>();

    public Path() {
        this(null);
    }

    public Path(Group parent) {
        this.parent = parent;
    }

    public int getPathLength() {
        return directions.stream().mapToInt(PathElement::getPathLength).sum();
    }

    public List<String> getPathStrings() {
        return getPathStrings("");
    }

    public List<String> getPathStrings(String head) {
        return getPathStringsInternal(head, new ArrayList<>(directions));
    }

    public List<String> getPathStrings(String head, List<PathElement> tail) {
        List<PathElement> elements = new ArrayList<>(directions);
        elements.addAll(tail);
        return getPathStringsInternal(head, elements);
    }

    public List<String> getPathStringsInternal(String head, List<PathElement> tail) {
        LinkedList<String> pathStrings = new LinkedList<>();
        while (!tail.isEmpty()) {
            PathElement p = tail.remove(0);

            if (p instanceof Direction) {
                if (Direction.canAddDirection(head, (Direction) p)) {
                    head = head + p;
                    // System.out.println(head);
                    pathStrings.add(head);

                } else {
                    break;
                }
            } else { // Group
                pathStrings.addAll(((Group) p).getPathStrings(head, tail));
            }
        }
        return pathStrings;
    }

    public void add(Character direction) {
        directions.add(new Direction(this, direction));
    }

    public Group newGroup() {
        Group g = new Group(this);
        directions.add(g);
        return g;
    }

    public Group getParent() {
        return parent;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if (parent == null) {
            b.append('^');
        }
        b.append(StringUtils.join(directions, ""));
        if (parent == null) {
            b.append('$');
        }
        return b.toString();
    }
}
