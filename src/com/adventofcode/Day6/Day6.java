package com.adventofcode.Day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day6 {

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day6/input.txt");
        String actual = Files.readAllLines(fileName).get(0);
        List<Integer> values = Arrays.stream(actual.split(","))
                .map(v -> Integer.valueOf(v))
                .collect(Collectors.toList());

        List<Long> lanternFish = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            lanternFish.add(0L);
        }

        for (int fish = 0; fish < values.size(); fish++) {
            int v = values.get(fish);
            lanternFish.set(v, lanternFish.get(v) + 1L);
        }

        for (int day=0; day<=256; day++) {
            nextDay(lanternFish);
        }

        long totalFish=0;
        for (int fish = 0; fish < lanternFish.size()-1; fish++) {
            totalFish+=lanternFish.get(fish);
        }
        System.out.println(totalFish);
    }

    private static void nextDay(List<Long> lanternFish) {
        Long doublingFish = lanternFish.get(0);
        for (int fish = 0; fish < lanternFish.size()-1; fish++) {
            lanternFish.set(fish, lanternFish.get(fish + 1));
        }
        lanternFish.set(6, lanternFish.get(6) + doublingFish);
        lanternFish.set(8, doublingFish);
    }
    
}
