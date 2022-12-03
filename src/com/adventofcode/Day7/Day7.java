package com.adventofcode.Day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day7 {

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day7/input.txt");
        String actual = Files.readAllLines(fileName).get(0);
        List<Integer> startingPositions = Arrays.stream(actual.split(","))
                .map(v -> Integer.valueOf(v))
                .sorted()
                .collect(Collectors.toList());

        findFuelCost(startingPositions);

        findExponentialFuelCost(startingPositions);

    }

    private static void findExponentialFuelCost(List<Integer> startingPositions) {
        int totalDistance = startingPositions.stream()
                .reduce(0, (accumulator, element) -> accumulator + element);
        int average = totalDistance/startingPositions.size();

        IntStream.of(5);

        int fuel = startingPositions.stream()
                .map(s -> Math.abs(s - average))
                .map(d -> IntStream.rangeClosed(1,d).sum())
                .reduce(0, (accumulator, element) -> accumulator + element);

        System.out.println(fuel);
    }

    private static void findFuelCost(List<Integer> startingPositions) {
        int median = startingPositions.get(startingPositions.size()/2);

        int fuel = startingPositions.stream()
                .map(s -> Math.abs(s - median))
                .reduce(0, (accumulator, element) -> accumulator + element);

        System.out.println(fuel);
    }

}
