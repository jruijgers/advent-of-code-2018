package org.salandur.advent_of_code;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class Day04 {
    private static final Pattern GUARD_STARTS_SHIFT = Pattern.compile("Guard #(\\d+) begins shift");
    private static final Pattern GUARD_FALLS_ASLEEP = Pattern.compile("00:(\\d{2})] falls asleep");
    private static final Pattern GUARD_WAKES_UP = Pattern.compile("00:(\\d{2})] wakes up");

    public static void main(String[] args) throws IOException {
        Path data = Main.pathFromClasspath("/day04.txt");

        List<GuardShift> allShifts = Files.lines(data).sorted().collect(Day04::new, Day04::accept, Day04::combine).shifts;

        Map<Guard, List<GuardShift>> guards = allShifts.stream().collect(groupingBy(GuardShift::getGuard));
        Map<Guard, Integer> totalSleepPerGuard = guards.entrySet().stream().collect(toMap(Map.Entry::getKey, Day04::totalGuardSleep));

        Guard mostSleepingGuard = findKeyWithMax(totalSleepPerGuard);
        Map<Integer, Integer> guardSleepsOnMinute = sleepPerMinute(guards.get(mostSleepingGuard));

        Integer sleepsTheMostAt = findKeyWithMax(guardSleepsOnMinute);
        Integer timesAsleep = guardSleepsOnMinute.get(sleepsTheMostAt);

        System.out.printf("Day 4.1: Guard %s sleeps the most, at 00:%02d, %d times. Answer is %d\n", mostSleepingGuard, sleepsTheMostAt, timesAsleep, mostSleepingGuard.getId() * sleepsTheMostAt);

        Map<Guard, Map<Integer, Integer>> guardSleepPerMinute = guards.entrySet().stream().collect(toMap(Map.Entry::getKey, Day04::sleepPerMinute));
        Map<Integer, Map<Guard, Integer>> minuteGuardsSleep = guardSleepPerMinute.entrySet().stream().flatMap(Day04::toMinuteAndGuard).collect(toMap(Map.Entry::getKey, Day04::guardMinuteValueMapper, Day04::combineGuardMinutes));
        Optional<Map.Entry<Integer, Map<Guard, Integer>>> guardsSleepsTheMostOnMinute = minuteGuardsSleep.entrySet().stream().reduce(Day04::reduceGuardsSleep);

        mostSleepingGuard = findKeyWithMax(guardsSleepsTheMostOnMinute.get().getValue());
        timesAsleep = guardsSleepsTheMostOnMinute.get().getValue().get(mostSleepingGuard);
        sleepsTheMostAt = guardsSleepsTheMostOnMinute.get().getKey();

        System.out.printf("Day 4.2: Guard %s sleeps the most at 00:%02d, %d times. Answer is %d\n", mostSleepingGuard, sleepsTheMostAt, timesAsleep, mostSleepingGuard.getId() * sleepsTheMostAt);
    }

    private static Map.Entry<Integer, Map<Guard, Integer>> reduceGuardsSleep(Map.Entry<Integer, Map<Guard, Integer>> x, Map.Entry<Integer, Map<Guard, Integer>> y) {
        Integer maxX = x.getValue().get(findKeyWithMax(x.getValue()));
        Integer maxY = y.getValue().get(findKeyWithMax(y.getValue()));

        return maxX > maxY ? x : y;
    }

    private static Map<Guard, Integer> guardMinuteValueMapper(Map.Entry<Integer, Map<Guard, Integer>> t) {
        return t.getValue();
    }

    private static Map<Guard, Integer> combineGuardMinutes(Map<Guard, Integer> u, Map<Guard, Integer> u1) {
        for (Map.Entry<Guard, Integer> e:u1.entrySet()) {
            u.put(e.getKey(), e.getValue());
        }
        return u;
    }

    private static Stream<Map.Entry<Integer, Map<Guard, Integer>>> toMinuteAndGuard(Map.Entry<Guard, Map<Integer, Integer>> entry) {
        Map<Integer, Map<Guard, Integer>> result = new HashMap<>();

        for (Map.Entry<Integer, Integer> x : entry.getValue().entrySet()) {
            result.putIfAbsent(x.getKey(), new HashMap<>());
            result.get(x.getKey()).put(entry.getKey(), x.getValue());
        }

        return result.entrySet().stream();
    }


    private static <T> T findKeyWithMax(Map<T, Integer> map) {
        return map.entrySet().stream().max((l, r) -> l.getValue() - r.getValue()).get().getKey();
    }

    private static Stream<Integer> doSleepingMinuts(GuardShift guardShift) {
        return guardShift.sleepingMinutes.stream();
    }

    private static void accept(Day04 results, String input) {
        Matcher guardNumber = GUARD_STARTS_SHIFT.matcher(input);
        Matcher fallsAsleep = GUARD_FALLS_ASLEEP.matcher(input);
        Matcher wakesUp = GUARD_WAKES_UP.matcher(input);

        if (guardNumber.find()) {
            results.addNewShift(guardNumber.group(1));
        } else if (fallsAsleep.find()) {
            results.lastShift.fallAsleep(Integer.valueOf(fallsAsleep.group(1)));
        } else if (wakesUp.find()) {
            results.lastShift.wakesUp(Integer.valueOf(wakesUp.group(1)));
        } else {
            throw new RuntimeException("No matches found for '" + input + "'");
        }
    }

    private static int totalGuardSleep(Map.Entry<Guard, List<GuardShift>> guardShifts) {
        return guardShifts.getValue().stream().mapToInt(gs -> gs.sleepingMinutes.size()).sum();
    }

    private static Map<Integer, Integer> sleepPerMinute(Map.Entry<Guard, List<GuardShift>> guardShifts) {
        return sleepPerMinute(guardShifts.getValue());
    }

    private static Map<Integer, Integer> sleepPerMinute(List<GuardShift> guardShifts) {
        return guardShifts.stream().flatMap(Day04::doSleepingMinuts).collect(groupingBy(Function.identity(), summingInt(e -> 1)));
    }

    private static void combine(Day04 left, Day04 right) {
        left.shifts.addAll(right.shifts);
    }

    private GuardShift lastShift;
    private List<GuardShift> shifts = new ArrayList<>();

    private void addNewShift(String guardNumber) {
        lastShift = new GuardShift(guardNumber);
        shifts.add(lastShift);
    }

    private static class Guard {
        private final Integer id;

        public Guard(String id) {
            this.id = Integer.valueOf(id);
        }

        public Integer getId() {
            return id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Guard guard = (Guard) o;
            return Objects.equals(id, guard.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        public String toString() {
            return "#" + id;
        }
    }

    private static class GuardShift {
        private final Guard guard;
        private List<Integer> sleepingMinutes = new ArrayList<>();

        private Integer lastFallAsleep = null;

        public GuardShift(String guardNumber) {
            this.guard = new Guard(guardNumber);
        }

        public Guard getGuard() {
            return guard;
        }

        public void fallAsleep(Integer minute) {
            lastFallAsleep = minute;
        }

        public void wakesUp(Integer minute) {
            sleepingMinutes.addAll(IntStream.range(lastFallAsleep, minute).boxed().collect(toList()));
        }

        @Override
        public String toString() {
            return "GuardShift[" + guard + "," + sleepingMinutes.size() + "]";
        }
    }
}
