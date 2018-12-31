package org.salandur.advent_of_code.day20;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;

import static java.util.stream.Collectors.toList;

public class Group implements PathPart {
    private final Path parent;
    private final LinkedList<Path> paths = new LinkedList<>();

    public Group(Path parent) {
        this.parent = parent;
    }

    @Override
    public int getPathLength() {
        return paths.parallelStream().mapToInt(Path::getPathLength).sum();
    }

    @Override
    public int getLongestPathLength() {
        return isCyclic() ? 0 : paths.parallelStream().mapToInt(Path::getLongestPathLength).max().getAsInt();
    }

    private boolean isCyclic() {
        for (Path p : paths) {
            if (p.getPathLength() == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getPathLengths() {
        StringBuilder b = new StringBuilder();
        b.append('(');
        b.append(StringUtils.join(paths.stream().map(p -> p.getPathLengths()).collect(toList()), '|'));
        b.append(')');
        return b.toString();
    }

    public Path newPath() {
        Path p = new Path(this);
        paths.add(p);
        return p;
    }

    public Path lastPath() {
        return paths.getLast();
    }

    public Path getParent() {
        return parent;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append('(');
        b.append(StringUtils.join(paths, "|"));
        b.append(')');
        return b.toString();
    }
}
