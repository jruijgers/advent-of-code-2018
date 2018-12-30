package org.salandur.advent_of_code;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Day11 {
    private static final int GRID_SIZE = 300;

    public static void main(String[] args) {
        PowerGrid powerGrid = new PowerGrid(2866);

        int gridSize = 3;
        PowerCell powerCell = powerGrid.findOptimumGrid(gridSize);
        System.out.printf("Day 11.1: the optimum for grid %dx%s is on location %d,%d (%d)\n", gridSize, gridSize, powerCell.x, powerCell.y, powerCell.calculatePowerGrid(gridSize));

        int optimumGridSize = powerGrid.findOptimumGridSize();
        PowerCell optimumCellCell = powerGrid.findOptimumGrid(optimumGridSize);
        System.out.printf("Day 11.2: the optimum for grid %dx%s is on location %d,%d (%d)\n", optimumGridSize, optimumGridSize, optimumCellCell.x, optimumCellCell.y, optimumCellCell.calculatePowerGrid(optimumGridSize));
    }

    private static class PowerGrid {
        private int gridSerialNumber;
        private List<PowerCell> grid;

        public PowerGrid(int gridSerialNumber) {
            this.gridSerialNumber = gridSerialNumber;
            grid = IntStream.range(0, GRID_SIZE * GRID_SIZE).parallel().mapToObj(i -> PowerCell.create(this, i)).collect(toList());
        }

        public PowerCell getPowerCell(int x, int y) {
            return grid.get(((y - 1) * GRID_SIZE) + (x - 1));
        }

        public int findOptimumGridSize() {
            int optimumGridSize = 2;
            for (int gridSize = 2; gridSize <= GRID_SIZE; gridSize++) {
                if (getGridPower(optimumGridSize) < getGridPower(gridSize)) {
                    optimumGridSize = gridSize;
                }
            }
            return optimumGridSize;
        }

        private int getGridPower(int gridSize) {
            return findOptimumGrid(gridSize).calculatePowerGrid(gridSize);
        }

        public PowerCell findOptimumGrid(int gridSize) {
            PowerCell max = grid.get(0);
            for (int i = 1; i < grid.size(); i++) {
                if (max.calculatePowerGrid(gridSize) < grid.get(i).calculatePowerGrid(gridSize)) {
                    max = grid.get(i);
                }
            }
            return max;
        }

    }

    private static class PowerCell {
        public static PowerCell create(PowerGrid grid, int i) {
            return new PowerCell(grid, (i % GRID_SIZE) + 1, (i / GRID_SIZE) + 1);
        }

        private final PowerGrid grid;
        private int x;
        private int y;
        private Integer power;

        private Map<Integer, Integer> optimumPowerGrids = new HashMap<>();

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

        public int calculatePowerGrid(int gridSize) {
            if (gridSize == 1) {
                return power;
            } else if (!optimumPowerGrids.containsKey(gridSize)) {
                int optimumPower = calculatePowerGrid(gridSize - 1);
                if (this.y + gridSize <= GRID_SIZE) {
                    for (int x = this.x; x < Math.min(GRID_SIZE, this.x + gridSize - 1); x++) {
                        // System.out.printf("%d: %dx%d\n", gridSize, x, this.y + gridSize - 1);
                        optimumPower += grid.getPowerCell(x, this.y + gridSize - 1).power;
                    }
                }
                if (this.x + gridSize <= GRID_SIZE) {
                    for (int y = this.y; y < Math.min(GRID_SIZE, this.y + gridSize - 1); y++) {
                        // System.out.printf("%d: %dx%d\n", gridSize, this.x + gridSize - 1, y);
                        optimumPower += grid.getPowerCell(this.x + gridSize - 1, y).power;
                    }
                }
                if (this.y + gridSize <= GRID_SIZE && this.x + gridSize <= GRID_SIZE) {
                    // System.out.printf("%d: %dx%d\n", gridSize, x + gridSize - 1, y + gridSize - 1);
                    optimumPower += grid.getPowerCell(this.x + gridSize - 1, this.y + gridSize - 1).power;
                }

                // System.out.printf("%d: %dx%d -> %d\n\n", gridSize, x, y, optimumPower);
                optimumPowerGrids.put(gridSize, optimumPower);
            }

            return optimumPowerGrids.get(gridSize);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                    .append("x", x)
                    .append("y", y)
                    .append("power", power)
                    .toString();
        }

    }
}
