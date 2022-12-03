package com.adventofcode.Day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day9 {

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day9/input.txt");
        List<int[]> actual = Files.readAllLines(fileName).stream()
                .map(line -> Arrays.stream(line.split("")).mapToInt(Integer::parseInt).toArray())
                .collect(Collectors.toList());

        int columns = actual.get(0).length;
        int rows = actual.size();

        int[][] map = new int[rows][columns];

        IntStream.range(0 , rows).forEach(r -> {
            map[r] = actual.get(r);
        });

        List<Integer> riskLevels = new ArrayList<>();
        List<Integer> basinSizes = new ArrayList<>();

        // loop over all the locations in the map
        IntStream.range(0 , rows).forEach(r -> {
            IntStream.range(0 , columns).forEach(c -> {
                if (isLowPoint(r,c,map)) {
                    // Part A
                    riskLevels.add(map[r][c] + 1);

                    // Part B
                    int[][] basinMap = new int[rows][columns];
                    createBasinMap(r,c,map,basinMap);
                    basinSizes.add(basinMapSize(basinMap));
                }
            });
        });

        // Part A
        int risk = riskLevels.stream().reduce(Integer::sum).get();
        System.out.println(risk);

        // Part B
        List<Integer> sorted = basinSizes.stream().sorted().collect(Collectors.toList());
        Collections.reverse(sorted);
        int result = sorted.get(0) * sorted.get(1) * sorted.get(2);
        System.out.println(result);

    }

    private static int basinMapSize(int[][] basinMap) {
        int size=0;
        for (int r=0; r<basinMap.length; r++) {
            for (int c=0; c<basinMap[r].length; c++) {
                size += basinMap[r][c];
            }
        }
        return size;
    }

    private static void createBasinMap(int row, int column, int[][] map, int[][] basinMap) {
        // set current location as 1
        basinMap[row][column] = 1;
        if (notTriedYet(row-1, column, basinMap) && getNeighbourValue(row-1, column, map) != 9) {
            // up
            createBasinMap(row-1, column, map, basinMap);
        }
        if (notTriedYet(row+1, column, basinMap) && getNeighbourValue(row+1, column, map) != 9) {
            // down
            createBasinMap(row+1, column, map, basinMap);
        }
        if (notTriedYet(row, column-1, basinMap) && getNeighbourValue(row, column-1, map) != 9) {
            // left
            createBasinMap(row, column-1, map, basinMap);
        }
        if (notTriedYet(row, column+1, basinMap) && getNeighbourValue(row, column+1, map) != 9) {
            // right
            createBasinMap(row, column+1, map, basinMap);
        }
    }

    private static boolean isLowPoint(int row, int column, int[][] map) {
        boolean lessThanAbove = map[row][column] < getNeighbourValue(row-1, column, map);
        boolean lessThanBelow = map[row][column] < getNeighbourValue(row+1, column, map);
        boolean lessThanRight = map[row][column] < getNeighbourValue(row, column+1, map);
        boolean lessThanLeft = map[row][column] < getNeighbourValue(row, column-1, map);

        return lessThanAbove && lessThanBelow && lessThanRight && lessThanLeft;
    }

    private static int getNeighbourValue(int row, int column, int[][] map) {
        try {
            return map[row][column];
        } catch (Exception e) {
            return 9;
        }
    }

    private static boolean notTriedYet(int row, int column, int[][] map) {
        try {
            return map[row][column] == 0;
        } catch (Exception e) {
            return false;
        }
    }




}
