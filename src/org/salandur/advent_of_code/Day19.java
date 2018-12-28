package org.salandur.advent_of_code;

import org.salandur.advent_of_code.time_travel_watch.Instruction;
import org.salandur.advent_of_code.time_travel_watch.Registers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class Day19 {
    public static void main(String[] args) throws IOException {
        Day19 day19 = new Day19();

        // day19.parseInput("day19.example.txt");
        day19.parseInput("day19.txt");

        day19.run();
    }

    private final Registers registers = new Registers(6);
    private final List<Instruction> instructions = new LinkedList<>();
    private long instructionsExecuted;
    private int instructionPointerRegister = 0;
    private int instructionPointer;

    private void run() {
        // int print = 20000;
        while (instructionPointer < instructions.size()) {
            synchronized (this) {
                registers.set(instructionPointerRegister, instructionPointer);

                // if (instructionsExecuted < print)
                //     System.out.printf("  [%,d]] IP[%2d] %s + %s ->", instructionsExecuted, instructionPointer, registers, instructions.get(instructionPointer));

                instructions.get(instructionPointer).execute(registers);

                // if (instructionsExecuted < print) System.out.println(registers);

                instructionPointer = registers.get(instructionPointerRegister) + 1;
                instructionsExecuted++;
            }
        }

        System.out.printf("Day 19: after executing the program register 0 contains %d (%,d instructions executed)\n", registers.get(0), instructionsExecuted);
    }

    private synchronized void printStatus() {
        System.out.printf("  [%,d]] IP[%2d] %s + %s\n", instructionsExecuted, instructionPointer, registers, instructions.get(instructionPointer));
    }

    private void parseInput(String input) throws IOException {
        instructions.clear();

        List<String> strings = Files.readAllLines(Path.of(input));

        instructionPointerRegister = Integer.parseInt(strings.remove(0).substring(4));

        for (String instruction : strings) {
            instructions.add(Instruction.parse(instruction));
        }
    }
}
