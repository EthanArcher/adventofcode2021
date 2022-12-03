package com.adventofcode.Day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day8 {

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day8/input.txt");
        List<String> actual = Files.readAllLines(fileName);
        List<Integer> counts = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            counts.add(0);
        }
        List<Integer> totals = new ArrayList<>();

        actual.stream().forEach(line -> countLine(line, counts));

        int total = counts.get(2) + counts.get(4) +counts.get(3) + counts.get(7);
        System.out.println(total);

        actual.stream().forEach(line -> countLine(line, counts));
        actual.stream().forEach(line -> translate(line, totals));

        System.out.println(totals.stream().reduce(0, Integer::sum));
    }

    private static void countLine(String line1, List<Integer> counts) {
        List<String> values = Arrays.asList(line1.split("\\|"));
        String output = values.get(1).trim();

        List<String> outputs = Arrays.asList(output.split(" "));
        outputs.stream().forEach(s -> {
            int seg = s.length();
            counts.set(seg, counts.get(seg) + 1);
        });
    }

    private static void translate(String line1, List<Integer> totals) {
        List<List<String>> numbers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            numbers.add(List.of(""));
        }

        List<String> values = Arrays.asList(line1.split("\\|"));
        List<List<String>> inputs = splitSegments(values.get(0).trim());
        List<List<String>> outputs = splitSegments(values.get(1).trim());

        // find 1,4,7,8
        inputs.stream().forEach(s -> {
            int illuminatedSegments = s.size();
            switch(illuminatedSegments) {
                case 2:
                    numbers.set(1, s);
                    break;
                case 3:
                    numbers.set(7, s);
                    break;
                case 4:
                    numbers.set(4, s);
                    break;
                case 7:
                    numbers.set(8, s);
                    break;
                default:
                    break;
            }

        });

        // find other values
        inputs.stream().forEach(s -> {
            int illuminatedSegments = s.size();
            long matchesWith4 = s.stream().filter(l -> numbers.get(4).contains(l)).count();
            switch(illuminatedSegments) {
                case 5:
                    if (s.containsAll(numbers.get(1))) {
                        numbers.set(3, s);
                    } else {
                        if (matchesWith4 == 2) {
                            numbers.set(2, s);
                        } else {
                            numbers.set(5, s);
                        }
                    }
                    break;
                case 6:
                    if (!s.containsAll(numbers.get(1))) {
                        numbers.set(6, s);
                    } else {
                        if (matchesWith4 == 4) {
                            numbers.set(9, s);
                        } else {
                            numbers.set(0, s);
                        }
                    }
                    break;
                default:
                    break;
            }
        });

        String result = outputs.stream()
                .map(number -> translateToNumber(number, numbers))
                .map(Object::toString)
                .reduce("", (a,b) -> a + b);

        totals.add(Integer.valueOf(result));
    }

    private static int translateToNumber(List<String> string, List<List<String>> numbers) {
        for (int i=0; i<10; i++) {
            if (numbers.get(i).equals(string)) {
                return i;
            }
        }
        return -1;
    }

    private static List<List<String>> splitSegments(String input) {
        return Arrays.asList(input.split(" ")).stream()
                .map(str -> str.split(""))
                .map(Arrays::asList)
                .map(str -> str.stream().sorted().collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

}
