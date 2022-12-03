package com.adventofcode.Day13;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 {

    static int maxColumn=0;
    static int maxRow=0;
    static List<String> coordinates = new ArrayList<>();
    static List<String> folds = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day13/input.txt");
        Files.readAllLines(fileName).forEach(line -> {
            if (line.contains(",")) {
                coordinates.add(line);
            } else if (line.contains("fold along")) {
                folds.add(line.split(" ")[2]);
            }
        });

        List<List<Integer>> actual = coordinates.stream()
                .map(line -> Arrays.stream(line.split(",")).map(Integer::valueOf).collect(Collectors.toList()))
                .collect(Collectors.toList());

        actual.forEach(line -> {
            maxRow = Math.max(maxRow, line.get(1));
            maxColumn = Math.max(maxColumn, line.get(0));
        });
        maxRow++;
        maxColumn++;

        // [row][column]
        int[][] paper = new int[maxRow][maxColumn];

        actual.forEach(line -> paper[line.get(1)][line.get(0)] = 1);

        int[][] res0 = doFold(paper, folds.get(0));
        count(res0);

        int[][] res = paper;
        for (String fold : folds) {
            int[][] temp = doFold(res, fold);
            res = temp;
        }

        //folds.forEach(fold -> doFold(paper, fold));


        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[i].length; j++) {
                if (res[i][j] > 0) System.out.print("#" + " ");
                else System.out.print("  ");
            }
            System.out.println();
        }
    }

    private static void count(int[][] res) {
        int total = 0;
        for (int r=0; r<res.length; r++) {
            for (int c=0; c<res[0].length; c++) {
                if (res[r][c] > 0) total++;
            }
        }
        System.out.println(total);
    }

    private static int[][] doFold(int[][] paper, String fold) {
        String[] f = fold.split("=");
        String axis = f[0];
        int value  = Integer.valueOf(f[1]);

        if (axis.equals("x")) {
            return foldLeft(paper, value);
        } else {
            return foldUp(paper, value);
        }
    }

    private static int[][] foldUp(int[][] paper, int value) {
        int rows = paper.length;
        int columns = paper[0].length;
        int[][] result = new int[value][columns];
        for (int r=0; r<value; r++) {
            for (int c=0; c<columns; c++) {
                result[r][c] = paper[r][c] + paper[rows-r-1][c];
            }
        }
        return result;
    }

    private static int[][] foldLeft(int[][] paper, int value) {
        int rows = paper.length;
        int columns = paper[0].length;
        int[][] result = new int[rows][value];
        for (int r=0; r<rows; r++) {
            for (int c=0; c<value; c++) {
                result[r][c] = paper[r][c] + paper[r][columns-c-1];
            }
        }
        return result;
    }
}
