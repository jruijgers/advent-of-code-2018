package org.salandur.advent_of_code;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Day11 {
    private static final int GRID_SIZE = 300;

    public static void main(String[] args) {
        PowerCell p = new PowerGrid(2866).findOptimumGrid();
        System.out.printf("Day 11.1: the optimum grid location is %d, %d (%d)",p.x-1, p.y-1, p.optimumPower);
    }

    private static class PowerGrid {
        private int gridSerialNumber;
        private List<PowerCell> grid;

        public PowerGrid(int gridSerialNumber) {
            this.gridSerialNumber = gridSerialNumber;
            grid = IntStream.range(0, GRID_SIZE * GRID_SIZE).parallel().mapToObj(this::toPowerCell).collect(toList());
        }

        public PowerCell findOptimumGrid() {
            PowerCell max = grid.get(0);
            for (int i = 1; i < grid.size(); i++) {
                if (max.calculatePowerGrid() < grid.get(i).calculatePowerGrid()) max = grid.get(i);
            }
            return max;
        }

        public PowerCell toPowerCell(int i) {
            return new PowerCell(this, (i % GRID_SIZE) + 1, (i / GRID_SIZE) + 1);
        }

        public PowerCell getPowerCell(int x, int y) {
            return grid.get(((y - 1) * GRID_SIZE) + (x - 1));
        }
    }

    private static class PowerCell {
        private final PowerGrid grid;
        private int x;
        private int y;
        private Integer power;
        private Integer optimumPower;

        public PowerCell(PowerGrid grid, int x, int y) {
            this.grid = grid;
            this.x = x;
            this.y = y;

            calculatePower();
        }

        private void calculatePower() {
            int rackId = x + 10;
            power = ((((rackId * y + grid.gridSerialNumber) * rackId) / 100) % 10) - 5;
        }

        public int calculatePowerGrid() {
            if (optimumPower == null) {
                optimumPower = 0;
                for (int x = this.x - 1; x <= this.x + 1; x++) {
                    for (int y = this.y - 1; y <= this.y + 1; y++) {
                        if (x > 0 && x <= GRID_SIZE &&y > 0 && y <= GRID_SIZE) {
                            optimumPower += grid.getPowerCell(x, y).power;
                        }
                    }
                }
            }

            return optimumPower;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("x", x)
                    .append("y", y)
                    .append("power", power)
                    .append("optimumPower", optimumPower)
                    .toString();
        }

    }
}
