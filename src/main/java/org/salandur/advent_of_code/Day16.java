package org.salandur.advent_of_code;

import org.apache.commons.lang3.StringUtils;
import org.salandur.advent_of_code.time_travel_watch.OpCode;
import org.salandur.advent_of_code.time_travel_watch.Registers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day16 {

    public static void main(String[] args) throws IOException {
        new Day16().
                processSamples("/day16.samples.txt").
                findOpCodes().run("/day16.input.txt");
    }

    private Registers registers = new Registers();
    private int[] instruction = new int[4];
    private Map<Integer, OpCode> opCodes = new HashMap<>();

    // testing input
    private boolean testing = false;
    private int samplesWith3OrMoreOpCodes;
    private Map<Integer, List<OpCode>> sampleOpCodes = new HashMap<>();

    private Day16 processSamples(String input) throws IOException {
        Files.lines(Main.pathFromClasspath(input)).forEach(this::processLine);

        System.out.printf("Day 16.1: samples with 3 or more opcodes: %d\n", samplesWith3OrMoreOpCodes);

        return this;
    }

    private Day16 findOpCodes() {
        while (opCodes.size() < 16) {
            Iterator<Map.Entry<Integer, List<OpCode>>> iterator = sampleOpCodes.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, List<OpCode>> curr = iterator.next();
                if (curr.getValue().size() == 1) {
                    OpCode opCode = curr.getValue().get(0);
                    opCodes.put(curr.getKey(), opCode);

                    for (List<OpCode> opCodes : sampleOpCodes.values()) {
                        opCodes.remove(opCode);
                    }

                    iterator.remove();
                }
            }
        }

        return this;
    }

    private Day16 run(String input) throws IOException {
        registers.reset();

        Files.lines(Main.pathFromClasspath(input)).forEach(this::processLine);

        System.out.printf("Day 16.2: after running the test program, register 0 contains %d\n", registers.get(0));

        return this;
    }

    private void processLine(String line) {
        if (line.startsWith("Before")) {
            setRegisters(toValues(line));
            testing = true;
        } else if (line.startsWith("After")) {
            findMatchingOpCodes(toValues(line));

            testing = false;
        } else if (!line.isEmpty()) {
            setInstruction(line);

            if (!testing) {
                executeInstruction(opCodes.get(instruction[0]));
            }
        }
    }

    private Integer[] toValues(String line) {
        List<Integer> values = new ArrayList<>(4);

        for (String i : line.substring(line.indexOf('[') + 1, line.length() - 1).split(", ")) {
            values.add(Integer.parseInt(i));
        }

        return values.toArray(new Integer[4]);
    }

    private void setRegisters(Integer[] values) {
        registers.set(values);
    }

    private void findMatchingOpCodes(Integer[] expectedState) {
        List<OpCode> possibleOpCodes = new ArrayList<>(OpCode.OP_CODES);

        // store initial state of register
        Integer[] originalRegisterValues = registers.getState();

        Iterator<OpCode> opCodes = possibleOpCodes.iterator();
        while (opCodes.hasNext()) {
            // execute opcode
            OpCode opCode = opCodes.next();
            executeInstruction(opCode);

            // verify result
            if (!Arrays.equals(registers.getState(), expectedState)) {
                opCodes.remove();
            }

            // reset register
            registers.set(originalRegisterValues);
        }

        if (possibleOpCodes.size() >= 3) {
            this.samplesWith3OrMoreOpCodes++;
        }

        possibleOpCodes.retainAll(sampleOpCodes.getOrDefault(instruction[0], OpCode.OP_CODES));
        this.sampleOpCodes.putIfAbsent(instruction[0], possibleOpCodes);
    }

    private void setInstruction(String line) {
        int instruction = 0;
        for (String i : StringUtils.split(line)) {
            this.instruction[instruction++] = Integer.parseInt(i);
        }
    }

    private void executeInstruction(OpCode code) {
        code.perform(registers, instruction[1], instruction[2], instruction[3]);
    }
}
