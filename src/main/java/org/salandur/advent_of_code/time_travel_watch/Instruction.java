package org.salandur.advent_of_code.time_travel_watch;

import org.apache.commons.lang3.StringUtils;

public class Instruction {
    public static Instruction parse(String instruction) {
        String[] parts = StringUtils.split(instruction, " ");
        return new Instruction(OpCode.find(parts[0]), Integer.valueOf(parts[1]), Integer.valueOf(parts[2]), Integer.valueOf(parts[3]));
    }

    private final OpCode opCode;
    private final int inputA;
    private final int inputB;
    private final int output;

    public Instruction(OpCode opCode, int inputA, int inputB, int output) {
        this.opCode = opCode;
        this.inputA = inputA;
        this.inputB = inputB;
        this.output = output;
    }

    public void execute(Registers registers) {
        opCode.perform(registers, inputA, inputB, output);
    }

    @Override
    public String toString() {
        return opCode + "(" + inputA + "," + inputB + "," + output + ")";
    }
}
