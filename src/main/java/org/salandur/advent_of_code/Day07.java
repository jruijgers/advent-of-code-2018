package org.salandur.advent_of_code;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class Day07 {
    private static final Pattern STEPS = Pattern.compile("Step ([A-Z]) must be finished before step ([A-Z]) can begin.");

    public static void main(String[] args) throws IOException {
        Map<String, Instruction> instructionsMap = new HashMap<>();

        Files.lines(Main.pathFromClasspath("/day07.txt")).
                filter(StringUtils::isNotBlank).
                forEach(s -> parseInstruction(s, instructionsMap));

        List<String> orderOfInstructions = determineOrderOfInstructions(instructionsMap.values());

        System.out.printf("Day 7.1: the instructions need to be followed in the following order: %s\n", StringUtils.join(orderOfInstructions, ""));
    }

    private static void parseInstruction(String s, Map<String, Instruction> instructions) {
        Matcher m = STEPS.matcher(s);

        if (m.find()) {
            var dependent = instructions.computeIfAbsent(m.group(2), i -> new Instruction(i));
            var dependOn = instructions.computeIfAbsent(m.group(1), i -> new Instruction(i));

            dependent.addDependsOn(dependOn);
            dependOn.addDependent(dependent);
        } else {
            throw new RuntimeException(s + " not matched...");
        }
    }

    private static List<String> determineOrderOfInstructions(Collection<Instruction> instructionsMap) {
        List<String> orderedInstructions = new ArrayList<>();
        Set<Instruction> availableInstructions = new HashSet<>();
        availableInstructions.addAll(findNextInstructions(instructionsMap).collect(toList()));

        while (availableInstructions.size() > 0) {
            Instruction nextInstruction = findNextInstructions(availableInstructions).findFirst().get();

            orderedInstructions.add(nextInstruction.stepIdentifier);
            availableInstructions.remove(nextInstruction);
            availableInstructions.addAll(nextInstruction.dependents);

            nextInstruction.dependents.forEach(i -> i.dependsOn.remove(nextInstruction));
        }

        return orderedInstructions;
    }

    private static Stream<Instruction> findNextInstructions(Collection<Instruction> availableInstructions) {
        return availableInstructions.stream().sorted(comparing(Instruction::getStepIdentifier)).filter(i -> i.dependsOn.isEmpty());
    }

    private static class Instruction {
        private final String stepIdentifier;
        private Set<Instruction> dependsOn = new HashSet<>();
        private Set<Instruction> dependents = new HashSet<>();

        private Instruction(String stepIdentifier) {
            this.stepIdentifier = stepIdentifier;
        }

        public String getStepIdentifier() {
            return stepIdentifier;
        }

        private void addDependsOn(Instruction stepIdentifier) {
            dependsOn.add(stepIdentifier);
        }

        private void addDependent(Instruction stepIdentifier) {
            dependents.add(stepIdentifier);
        }

        @Override
        public String toString() {
            return stepIdentifier + " <= " + dependsOn.stream().map(Instruction::getStepIdentifier).collect(toList()) + " => " + dependents.stream().map(Instruction::getStepIdentifier).collect(toList());
        }
    }
}
