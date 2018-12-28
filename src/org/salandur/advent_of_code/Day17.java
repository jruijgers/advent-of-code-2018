package org.salandur.advent_of_code;

import org.salandur.advent_of_code.common.Direction;
import org.salandur.advent_of_code.common.Point;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.salandur.advent_of_code.common.Direction.*;

public class Day17 {
    private static final Pattern LINE = Pattern.compile("[xy]=(\\d+), [xy]=(\\d+)..(\\d+)");

    private static final Character SAND = '.';
    private static final Character CLAY = '#';
    private static final Character FLOWING_WATER = '|';
    private static final Character STILL_WATER = '~';

    public static void main(String[] args) throws IOException {
        // Files.createDirectories(Path.of("log", "day17"));

        Day17 day17 = new Day17();
        day17.parseFile("day17.txt");

        // day17.printGround(Files.newOutputStream(Path.of("log", "day17", "start.txt"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));

        day17.letTheWaterFlow(0);
        day17.validate();

        // day17.printGround(Files.newOutputStream(Path.of("log", "day17", "result.txt"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
    }

    Map<Integer, Map<Integer, Character>> groundCompound = new TreeMap<>();
    private int minY;
    private int maxY;
    private int minX;
    private int maxX;
    private WaterFlow source;
    private WaterFlow current;

    private Day17 parseFile(String input) throws IOException {
        Files.lines(Path.of(input)).forEach(this::parseLine);

        minY = groundCompound.keySet().stream().mapToInt(Integer::intValue).min().getAsInt();
        maxY = groundCompound.keySet().stream().mapToInt(Integer::intValue).max().getAsInt();
        minX = groundCompound.values().stream().flatMapToInt(m -> m.keySet().stream().mapToInt(Integer::intValue)).min().getAsInt() - 1;
        maxX = groundCompound.values().stream().flatMapToInt(m -> m.keySet().stream().mapToInt(Integer::intValue)).max().getAsInt() + 1;

        source = new WaterFlow(new Point(500, minY - 1), null);
        current = source;

        return this;
    }

    private Day17 validate() {
        int numberOfWaterSquares = 0;
        int numberOfStillWaterSquares = 0;
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                Character compound = getCompound(new Point(x, y));
                if (compound == STILL_WATER || compound == FLOWING_WATER) {
                    numberOfWaterSquares++;

                    if (compound == STILL_WATER) {
                        numberOfStillWaterSquares++;
                    }
                }
            }
        }

        System.out.printf("Day 17.1: number of water squares is %d\n", numberOfWaterSquares);
        System.out.printf("Day 17.2: number of still water squares is %d\n", numberOfStillWaterSquares);

        return this;
    }

    private Day17 letTheWaterFlow(int sleep) {
        // start flowing of water

        Set<Point> visited = new HashSet<>();

        do {
            visited.add(current.head);

            while (getCompound(current.head) == STILL_WATER) {
                current = current.previous;
            }

            if (sleep > 0) {
                Main.sleep(sleep);
            }

            Point down = current.head.move(DOWN);
            if (getCompound(down) == FLOWING_WATER || getCompound(down) == ' ') {
                current = current.previous;
                continue;
            }

            if (!visited.contains(down) && getCompound(down) == SAND) {
                current = new WaterFlow(down, current);
                setCompound(down, FLOWING_WATER);
                continue;
            }

            Point right = current.head.move(RIGHT);
            if (!visited.contains(right) && getCompound(right) == SAND) {
                current = new WaterFlow(right, current);
                setCompound(right, FLOWING_WATER);
                continue;
            }

            Point left = current.head.move(LEFT);
            if (!visited.contains(left) && getCompound(left) == SAND) {
                current = new WaterFlow(left, current);
                setCompound(left, FLOWING_WATER);
                continue;
            }


            markAsStill(current.head);
            current = current.previous;
        } while (current != source);

        return this;
    }

    private void markAsStill(Point head) {
        if (isEnclosedLine(head)) {
            setCompound(head, STILL_WATER);
            markAsStill(head.move(LEFT), LEFT);
            markAsStill(head.move(RIGHT), RIGHT);
        }
    }

    private boolean isEnclosedLine(Point head) {
        boolean isEnclosed = getCompound(head) == FLOWING_WATER;

        Character compound = getCompound(head.move(DOWN));
        isEnclosed &= (compound == CLAY || compound == STILL_WATER);

        Point left = head.move(LEFT);
        while (isEnclosed && getCompound(left) != CLAY) {
            compound = getCompound(left.move(DOWN));
            isEnclosed &= (compound == CLAY || compound == STILL_WATER);
            left = left.move(left);
        }
        Point right = head.move(RIGHT);
        while (isEnclosed && getCompound(right) != CLAY) {
            compound = getCompound(right.move(DOWN));
            isEnclosed &= (compound == CLAY || compound == STILL_WATER);
            right = right.move(RIGHT);
        }

        return isEnclosed;
    }

    private void markAsStill(Point current, Direction dir) {
        while (getCompound(current) != CLAY) {
            setCompound(current, STILL_WATER);
            current = dir.move(current);
        }
    }

    private void parseLine(String line) {
        Matcher matcher = LINE.matcher(line);
        if (matcher.matches()) {
            Integer location = Integer.parseInt(matcher.group(1));
            for (int i = Integer.parseInt(matcher.group(2)); i <= Integer.parseInt(matcher.group(3)); i++) {
                if (line.startsWith("x")) {
                    setClay(location, i);
                } else {
                    setClay(i, location);
                }
            }
        }
    }

    public void printGroundThread() {
        while (true) {
            if (current != null) {
                Main.clearTerminal();
                printGround(System.out, current.head.y() - 25, current.head.y() + 25, current.head.x() - 50, current.head.x() + 50);
                Main.sleep(50);
            }
        }
    }

    private void printGround(OutputStream out) throws IOException {
        printGround(new PrintStream(out), minY, maxY, minX, maxX);
        out.close();
    }

    private void printGround(PrintStream out, int minY, int maxY, int minX, int maxX) {
        out.printf("%dx%d .. %dx%d\n", this.minX, this.minY, this.maxX, this.maxY);
        out.printf("%dx%d .. %dx%d\n", max(minX, this.minX), max(minY, this.minY), min(maxX, this.maxX), min(maxY, this.maxY));

        for (int y = max(minY, this.minY); y <= min(maxY, this.maxY); y++) {
            for (int x = max(minX, this.minX); x <= min(maxX, this.maxX); x++) {
                if (x == current.head.x() && y == current.head.y()) {
                    out.print('\u25bc');
                } else {
                    out.print(getCompound(new Point(x, y)));
                }
            }
            out.println();
        }

        out.flush();
    }

    private void setClay(Integer x, Integer y) {
        setCompound(x, y, CLAY);
    }

    private void setCompound(Point location, Character type) {
        if (location.x() >= minX && location.x() <= maxX && location.y() >= minY && location.y() <= maxY) {
            setCompound(location.x(), location.y(), type);
        }
    }

    private void setCompound(Integer x, Integer y, Character type) {
        Map<Integer, Character> map = groundCompound.getOrDefault(y, new TreeMap<>());
        map.put(x, type);
        groundCompound.put(y, map);
    }

    private Character getCompound(Point location) {
        if (location.x() >= minX && location.x() <= maxX && location.y() >= minY && location.y() <= maxY) {
            return groundCompound.getOrDefault(location.y(), new TreeMap<>()).getOrDefault(location.x(), SAND);
        } else {
            return ' ';
        }
    }

    private class WaterFlow {
        private final Point head;
        private final WaterFlow previous;

        public WaterFlow(Point head, WaterFlow previous) {
            this.head = head;
            this.previous = previous;
        }
    }
}
