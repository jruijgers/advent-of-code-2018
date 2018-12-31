package org.salandur.advent_of_code.day20;

import java.util.List;

public interface PathElement {
    int getPathLength();

    List<String> getPathStrings(String head, List<PathElement> tail);
}
