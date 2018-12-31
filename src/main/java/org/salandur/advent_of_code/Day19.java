package org.salandur.advent_of_code;

import org.salandur.advent_of_code.time_travel_watch.Instruction;
import org.salandur.advent_of_code.time_travel_watch.Registers;

import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

public class Day19 {
    public static void main(String[] args) throws IOException {
        Day19 day19 = new Day19();

        //day19.parseInput("/day19.example.txt");

        // Original program
        // day19.parseInput("/day19.txt");
        // Optimized program
        day19.parseInput("/day19.optimized.txt");
        day19.reset(0);
        day19.run();
        day19.reset(1);
        day19.run(); // original program takes to long, only use with optimized program
    }

    private final Registers registers = new Registers(6);
    private final List<Instruction> instructions = new LinkedList<>();
    private long instructionsExecuted;
    private int instructionPointerRegister = 0;
    private int instructionPointer;

    private void reset(int initialRegister0Value) {
        registers.reset();
        registers.set(0, initialRegister0Value);

        instructionPointer = 0;
        instructionsExecuted = 0;
    }

    private void run() {
        // int print = 0;
        // int loopPrint = 10000000;
        while (instructionPointer < instructions.size()) {
            synchronized (this) {
                registers.set(instructionPointerRegister, instructionPointer);

                // if (instructionsExecuted <= print || instructionsExecuted % loopPrint == 0)
                //     System.out.printf("  [%,d]] IP[%2d] %s + %s ->", instructionsExecuted, instructionPointer, registers, instructions.get(instructionPointer));

                instructions.get(instructionPointer).execute(registers);

                // if (instructionsExecuted < print || instructionsExecuted % loopPrint == 0)
                //     System.out.println(registers);

                instructionPointer = registers.get(instructionPointerRegister) + 1;
                instructionsExecuted++;
            }
        }

        // System.out.println(registers);
        System.out.printf("Day 19: the sum of dividers for %d is %d (%,d instructions executed)\n", registers.get(1), registers.get(0), instructionsExecuted);
    }

    private synchronized void printStatus() {
        System.out.printf("  [%,d]] IP[%2d] %s + %s\n", instructionsExecuted, instructionPointer, registers, instructions.get(instructionPointer));
    }

    private void parseInput(String input) throws IOException {
        instructions.clear();

        List<String> strings = Files.readAllLines(Main.pathFromClasspath(input));

        instructionPointerRegister = Integer.parseInt(strings.remove(0).substring(4));

        for (String instruction : strings) {
            instructions.add(Instruction.parse(instruction));
        }
    }
}
