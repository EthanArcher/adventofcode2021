package com.adventofcode.Day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day11 {

    private static int rows;
    private static int columns;

    private static int flashes = 0;

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day11/input.txt");
        List<int[]> actual = Files.readAllLines(fileName).stream()
                .map(line -> stringToIntArray(line))
                .collect(Collectors.toList());
        int[][] grid = new int[actual.size()][];
        grid = actual.toArray(grid);

        rows = actual.size();
        columns = actual.get(0).length;

        for (int times=0; times<300; times++) {
            incrementAll(grid);
            flash(grid);
            if (allZero(grid)) {
                System.out.println("times:" + (times + 1));
                break;
            }
        }

        printArray(grid);
        System.out.println(flashes);

    }

    private static boolean allZero(int[][] grid) {
        boolean allZero = true;
        int rows = grid.length;
        int columns = grid[0].length;
        for (int r=0; r<rows; r++) {
            for (int c=0; c<columns; c++) {
                if (grid[r][c] != 0) {
                    allZero = false;
                }
            }
        }
        return allZero;
    }

    private static void incrementAll(int[][] grid) {
        int rows = grid.length;
        int columns = grid[0].length;
        for (int r=0; r<rows; r++) {
            for (int c=0; c<columns; c++) {
                if (grid[r][c] != 10) {
                    grid[r][c] = grid[r][c] + 1;
                }
            }
        }
    }

    private static void flash(int[][] grid) {
        boolean flashed = false;
        for (int r=0; r<rows; r++) {
            for (int c=0; c<columns; c++) {
                if (grid[r][c] > 9 && grid[r][c] < 100) {
                    flashes++;
                    incrementSurrounding(grid, r, c);
                    grid[r][c] = 101;
                    flashed = true;
                }
            }
        }
        if (flashed) {
            flash(grid);
        }
        // clear down
        for (int r=0; r<rows; r++) {
            for (int c=0; c<columns; c++) {
                if (grid[r][c] > 100) {
                    grid[r][c] = 0;
                }
            }
        }
    }

    private static void printArray(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void incrementSurrounding(int[][] grid, int a, int b) {
        for (int r=a-1; r<=a+1; r++) {
            for (int c=b-1; c<=b+1; c++) {
                if ((r >= 0 && r < grid.length) && (c >= 0 && c < grid[0].length)) {
                    if (grid[r][c] != 10) {
                        grid[r][c] = grid[r][c] + 1;
                    }
                }
            }
        }
    }

    private static int[] stringToIntArray(String line) {
        String[] s = line.split("");
        return Arrays.stream(s).mapToInt(Integer::parseInt).toArray();
    }

}
