package com.adventofcode.Day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day2 {

    static int position = 0;
    static int depth = 0;
    static int aim = 0;

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day2/input.txt");
        List<String> actual = Files.readAllLines(fileName);

        //Part A
        actual.stream().forEach(v -> applyMovementA(v));
        System.out.println(position * depth);

        //Part B
        position = 0;
        depth = 0;
        aim = 0;

        actual.stream().forEach(v -> applyMovementB(v));
        System.out.println(position * depth);
    }

    private static void applyMovementA(String v) {
        String[] result = v.split(" ");
        switch (result[0]) {
            case "forward":
                position = position + Integer.valueOf(result[1]);
                break;
            case "up":
                depth = depth - Integer.valueOf(result[1]);
                break;
            case "down":
                depth = depth + Integer.valueOf(result[1]);
                break;
        }
    }

    private static void applyMovementB(String v) {
        String[] result = v.split(" ");
        switch (result[0]) {
            case "forward":
                position = position + Integer.valueOf(result[1]);
                depth = depth + (aim * Integer.valueOf(result[1]));
                break;
            case "up":
                aim = aim - Integer.valueOf(result[1]);
                break;
            case "down":
                aim = aim + Integer.valueOf(result[1]);
                break;
        }
    }
}
