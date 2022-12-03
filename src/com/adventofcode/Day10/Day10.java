package com.adventofcode.Day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day10 {

    private static final String openSquare = "[";
    private static final String openRound = "(";
    private static final String openCurly = "{";
    private static final String openAngle = "<";
    private static final String closeSquare = "]";
    private static final String closeRound = ")";
    private static final String closeCurly = "}";
    private static final String closeAngle = ">";

    private static final List<String> closeList = List.of(closeSquare, closeRound, closeCurly, closeAngle);
    private static final List<String> unexpected = new ArrayList<>();
    private static final List<List<String>> incomplete = new ArrayList<>();
    private static final List<Long> incompleteTotals = new ArrayList<>();
    
    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day10/input.txt");
        List<List<String>> actual = Files.readAllLines(fileName).stream()
                .map(line -> lineToList(line))
                .collect(Collectors.toList());

        // Part A
        actual.forEach(Day10::removeSets);
        findTotal(unexpected);

        // Part B
        completeLines(incomplete);
    }

    private static void completeLines(List<List<String>> incomplete) {
        incomplete.forEach(Day10::completeLine);
        long middle = incompleteTotals.stream().sorted().collect(Collectors.toList()).get(incompleteTotals.size()/2);
        System.out.println(middle);
    }

    private static void completeLine(List<String> line) {
        Collections.reverse(line);
        long total = line.stream()
                .map(value -> getClosingValue(value))
                .reduce(0L, (acc, value) -> (acc * 5) + value);
        incompleteTotals.add(total);
    }

    private static void findTotal(List<String> unexpected) {
        int total = unexpected.stream().map(b -> getValue(b)).reduce(0, Integer::sum);
        System.out.println(total);
    }

    private static List<String> lineToList(String line) {
        List<String> s = new ArrayList<String>();
        s.addAll(Arrays.asList(line.split("")));
        return s;
    }

    private static void removeSets(List<String> line) {
        for (int i=1; i< line.size(); i++) {
            String open = line.get(i-1);
            String close = line.get(i);
            if (closeList.contains(close)) {
                if (isSet(open, close)) {
                    line.remove(i);
                    line.remove(i - 1);
                    if (line.size() > 0) {
                        removeSets(line);
                    }
                    break;
                } else {
                    unexpected.add(line.get(i));
                    break;
                }
            }
            if (i == line.size()-1) {
                incomplete.add(line);
            }
        }
    }

    private static boolean isSet(String open, String close) {
        switch(open) {
            case openSquare:
                return close.equals(closeSquare);
            case openRound:
                return close.equals(closeRound);
            case openCurly:
                return close.equals(closeCurly);
            case openAngle:
                return close.equals(closeAngle);
            default:
                return false;
        }
    }

    private static int getValue(String close) {
        switch(close) {
            case closeSquare:
                return 57;
            case closeRound:
                return 3;
            case closeCurly:
                return 1197;
            case closeAngle:
                return 25137;
            default:
                return 0;
        }
    }

    private static Long getClosingValue(String close) {
        switch(close) {
            case openRound:
                return 1L;
            case openSquare:
                return 2L;
            case openCurly:
                return 3L;
            case openAngle:
                return 4L;
            default:
                return 0L;
        }
    }

}
