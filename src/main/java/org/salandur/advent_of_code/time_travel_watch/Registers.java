package org.salandur.advent_of_code.time_travel_watch;

import java.util.Arrays;

public final class Registers {
    private final Integer[] registers;

    public Registers() {
        this(4);
    }

    public Registers(int numberOfRegisters) {
        registers = new Integer[numberOfRegisters];
        reset();
    }

    public int get(int register) {
        return registers[register];
    }

    public void set(int register, int value) {
        registers[register] = value;
    }

    public void set(Integer[] values) {
        for (int i = 0; i < registers.length; i++) {
            registers[i] = values[i];
        }
    }

    public Integer[] getState() {
        return Arrays.copyOf(registers, registers.length);
    }

    @Override
    public String toString() {
        return Arrays.toString(registers);
    }

    public void reset() {
        for (int i = 0; i < registers.length; i++) {
            registers[i] = 0;
        }
    }
}
