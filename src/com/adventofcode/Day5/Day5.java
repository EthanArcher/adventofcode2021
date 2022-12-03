package com.adventofcode.Day5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day5 {

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day5/input.txt");
        List<String> actual = Files.readAllLines(fileName);
        int arraySize = 1000;
        int[][] grid = new int[arraySize][arraySize];

        List<List<Coordinate>> res = actual.stream()
                .map(line -> {
                    String[] temp = line.split("->");
                    return List.of(new Coordinate(temp[0]), new Coordinate(temp[1]));
                })
                .collect(Collectors.toList());

        res.stream().forEach(move -> {
            int startX = move.get(0).getX();
            int startY = move.get(0).getY();
            int endX = move.get(1).getX();
            int endY = move.get(1).getY();

            if ((startX == endX) || (startY == endY)) {
                markVents(grid, startX, startY, endX, endY);
            } else {
                // enable or disable for diagonals
                markVents(grid, startX, startY, endX, endY);
            }
        });

        //Arrays.stream(grid).forEach(line -> System.out.println(Arrays.toString(line)));

        int dangerPoints = 0;
        for (int x=0; x<arraySize; x++) {
            for (int y=0; y<arraySize; y++) {
                if (grid[x][y] >= 2) dangerPoints++;
            }
        }
        System.out.println(dangerPoints);

    }

    private static void markVents(int[][] grid, int startX, int startY, int endX, int endY) {
        int xDiff = getDiff(startX,endX);
        int yDiff = getDiff(startY,endY);

        int x = startX;
        int y = startY;
        grid[y][x]++;

        while(x != endX || y != endY) {
            x = x + xDiff;
            y = y + yDiff;
            grid[y][x]++;
        }
    }

    private static int getDiff(int start, int end) {
        if (start == end) {
            return 0;
        } else {
            return start < end ? 1 : -1;
        }
    }

    private static class Coordinate {

        int x,y;

        public Coordinate(String cs) {
            String[] value = cs.split(",");
            this.x = Integer.valueOf(value[0].trim());
            this.y = Integer.valueOf(value[1].trim());
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

}
