package com.adventofcode.Day1;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 {

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day1/input.txt");
        List<String> actual = Files.readAllLines(fileName);
        List<Integer> ints = actual.stream().map(s -> Integer.valueOf(s)).collect(Collectors.toList());
        partA(ints);
        partB(ints);
    }

    private static void partA(List<Integer> ints) {
        int result =0;

        for (int i=0; i<ints.size()-1; i++) {
            if (ints.get(i+1) > ints.get(i)) {
                result++;
            }
        }

        System.out.println(result);
    }

    private static void partB(List<Integer> ints) {
        List<Integer> avs = new ArrayList<>();
        for (int i=0; i<ints.size()-2; i++) {
            avs.add(ints.get(i) + ints.get(i+1) + ints.get(i+2));
        }
        partA(avs);
    }
}
