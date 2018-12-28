package org.salandur.advent_of_code.time_travel_watch;

import java.util.Arrays;

public final class Registers {
    private final Integer[] registers = new Integer[4];

    public Registers() {
        reset();
    }

    public int get(int register) {
        return registers[register];
    }

    public void set(int register, int value) {
        registers[register] = value;
    }

    public void set(Integer[] values) {
        for (int i = 0; i < 4; i++) {
            registers[i] = values[i];
        }
    }

    public Integer[] getState() {
        return Arrays.copyOf(registers, 4);
    }

    @Override
    public String toString() {
        return Arrays.toString(registers);
    }

    public void reset() {
        set(new Integer[]{0, 0, 0, 0});
    }
}
