package com.adventofcode.Day15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day15 {

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day15/input.txt");
        List<int[]> actual = Files.readAllLines(fileName).stream()
                .map(line -> Arrays.stream(line.split("")).mapToInt(Integer::parseInt).toArray())
                .collect(Collectors.toList());

        int[][] grid = new int[actual.size()][];
        grid = actual.toArray(grid);

        dijkstra(grid);

        int[][] bigGrid = new int[grid.length*5][grid[0].length*5];
        tessellateGrid(grid, bigGrid);

        dijkstra(bigGrid);

    }

    private static void dijkstra(int[][]grid) {
        int size = grid.length * grid[0].length;
        int[] distances = new int[size];
        boolean[] visited = new boolean[size];

        for (int i = 0; i < size; i++) {
            distances[i] = Integer.MAX_VALUE;
            visited[i] = false;
        }

        distances[0] = 0; // starting position

        for (int position = 0; position < size; position++) {
            //System.out.println("Completed:" + position + " of:" + size);
            // find the next closest unprocessed value
            int u = minDistanceIndex(distances, visited);
            visited[u] = true;

            for (int v = 0; v < size; v++) {
                // update distance[v] if not yet visited && edge exists && its a shorter path
                int connection = getConnection(u, v, grid);
                if (!visited[v] && connection != 0 && distances[u] != Integer.MAX_VALUE && distances[u] + connection < distances[v]) {
                    distances[v] = distances[u] + connection;
                }
            }
        }
        System.out.println(distances[size-1]);
    }

    private static int getConnection(int pos, int neighbour, int[][] grid) {
        int rows = grid.length;
        int columns = grid[0].length;
        int row = pos / rows;
        int column = pos % columns;

        if (pos-neighbour == rows) { // above
            return getNeighbour(row - 1, column, grid);
        }
        else if (neighbour-pos == rows) { // below
            return getNeighbour(row + 1, column, grid);
        }
        else if (pos-neighbour == 1) { // left
            return getNeighbour(row, column - 1, grid);
        }
        else if (neighbour-pos == 1) { // right
            return getNeighbour(row, column + 1, grid);
        }
        else {
            return 0;
        }

    }

    private static int minDistanceIndex(int distances[], boolean visited[]) {
        int min = Integer.MAX_VALUE;
        int min_index = -1;

        for (int v = 0; v < distances.length; v++)
            if (visited[v] == false && distances[v] <= min) {
                min = distances[v];
                min_index = v;
            }

        return min_index;
    }

    private static int getNeighbour(int r, int c, int[][] grid) {
        if ((r >= 0) && (r < grid.length)) {
            if ((c >= 0) && (c < grid[0].length)) {
                return grid[r][c];
            }
        }
        return 0;
    }

    private static void tessellateGrid(int[][] grid, int[][] bigGrid) {
        int rows = grid.length;
        int columns = grid[0].length;
        for (int rt=0; rt<5; rt++) {
            int nextTileRow = rt * rows;
            for (int tile=0; tile<5; tile++) {
                int nextTileColumn = tile * columns;
                for (int r=0; r < rows; r++) {
                    for (int c=0; c < columns; c++) {
                        int v = grid[r][c] + tile + rt;
                        bigGrid[r + nextTileRow][c + nextTileColumn] = v%10 + v/10;
                    }
                }
            }
        }
    }

}
