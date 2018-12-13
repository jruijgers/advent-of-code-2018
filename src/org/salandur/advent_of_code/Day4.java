package org.salandur.advent_of_code;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class Day4 {
    private static final Pattern GUARD_STARTS_SHIFT = Pattern.compile("Guard #(\\d+) begins shift");
    private static final Pattern GUARD_FALLS_ASLEEP = Pattern.compile("00:(\\d{2})] falls asleep");
    private static final Pattern GUARD_WAKES_UP = Pattern.compile("00:(\\d{2})] wakes up");

    public static void main(String[] args) throws IOException {
        Path data = Path.of("day4.txt");

        List<GuardShift> allShifts = Files.lines(data).sorted().collect(Day4::new, Day4::accept, Day4::combine).shifts;

        Map<Integer, List<GuardShift>> guards = allShifts.stream().collect(groupingBy(GuardShift::getGuardNumber));
        Map<Integer, Integer> totalSleepPerGuard = guards.entrySet().stream().collect(toMap(Map.Entry::getKey, Day4::totalGuardSleep));

        Integer mostSleepingGuard = findKeyWithMax(totalSleepPerGuard);
        Map<Integer, Integer> guardSleepsOnMinute = sleepPerMinute(guards.get(mostSleepingGuard));

        Integer sleepsTheMostAt = findKeyWithMax(guardSleepsOnMinute);

        System.out.printf("Day 4.1: Guard #%d sleeps the most, at 00:%02d, %d times. Answer is %d\n", mostSleepingGuard, sleepsTheMostAt, guardSleepsOnMinute.get(sleepsTheMostAt), mostSleepingGuard * sleepsTheMostAt);
    }

    private static <T> T findKeyWithMax(Map<T, Integer> map) {
        return map.entrySet().stream().max((l, r) -> l.getValue() - r.getValue()).get().getKey();
    }

    private static Stream<Integer> doSleepingMinuts(GuardShift guardShift) {
        return guardShift.sleepingMinutes.stream();
    }

    private static void accept(Day4 results, String input) {
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

    private static int totalGuardSleep(Map.Entry<Integer, List<GuardShift>> guardShifts) {
        return guardShifts.getValue().stream().mapToInt(gs -> gs.sleepingMinutes.size()).sum();
    }

    private static Map<Integer, Integer> sleepPerMinute(List<GuardShift> guardShifts) {
        return guardShifts.stream().flatMap(Day4::doSleepingMinuts).collect(groupingBy(Function.identity(), summingInt(e -> 1)));
    }

    private static void combine(Day4 left, Day4 right) {
        left.shifts.addAll(right.shifts);
    }

    private GuardShift lastShift;
    private List<GuardShift> shifts = new ArrayList<>();

    private void addNewShift(String guardNumber) {
        lastShift = new GuardShift(guardNumber);
        shifts.add(lastShift);
    }

    private static class GuardShift {
        private final Integer guardNumber;
        private List<Integer> sleepingMinutes = new ArrayList<>();

        private Integer lastFallAsleep = null;

        public GuardShift(String guardNumber) {
            this.guardNumber = Integer.valueOf(guardNumber);
        }

        public Integer getGuardNumber() {
            return guardNumber;
        }

        public List<Integer> getSleepingMinutes() {
            return sleepingMinutes;
        }

        public void fallAsleep(Integer minute) {
            lastFallAsleep = minute;
        }

        public void wakesUp(Integer minute) {
            sleepingMinutes.addAll(IntStream.range(lastFallAsleep, minute).boxed().collect(toList()));
        }

        @Override
        public String toString() {
            return "GuardShift[#" + guardNumber + "," + sleepingMinutes.size() + "]";
        }
    }
}
