package org.salandur.advent_of_code.day20;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;

public class Path implements PathPart {
    private final Group parent;
    private final LinkedList<PathPart> directions = new LinkedList<>();

    public Path() {
        this(null);
    }

    public Path(Group parent) {
        this.parent = parent;
    }

    @Override
    public int getPathLength() {
        return directions.stream().mapToInt(PathPart::getPathLength).sum();
    }

    public int getLongestPathLength() {
        return directions.stream().mapToInt(PathPart::getLongestPathLength).sum();
    }

    @Override
    public String getPathLengths() {
        StringBuilder b = new StringBuilder();
        int count = 0;
        for (PathPart p : directions) {
            if (p instanceof Direction) {
                count++;
            } else {
                if (count > 0) {
                    b.append(count);
                }
                count = 0;
                b.append(p.getPathLengths());
            }
        }
        if (b.length() == 0 || count > 0) {
            b.append(count);
        }
        return b.toString();
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
