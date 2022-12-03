package com.adventofcode.Day14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day14 {

    static List<String> rules = new ArrayList<>();
    static LinkedList<String> template = new LinkedList<>();
    static HashMap<String, Long> count = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day14/input.txt");
        List<String> input = Files.readAllLines(fileName);
        String templateString = input.get(0);
        String first = templateString.split("")[0];
        String last = templateString.split("")[templateString.length()-1];
        rules.addAll(input);
        rules.removeIf(line -> !line.contains("->"));

        Arrays.stream(templateString.split("")).forEach(s -> {
            template.add(s);
        });

        for (int i=1; i<template.size();i++) {
            addToMap(template.get(i-1) + template.get(i), 1L, count);
        }

        for (int i=0; i<40; i++){
            growPolymer();
        }
        
        HashMap<String, Long> totals = new HashMap<>();
        for (Map.Entry<String, Long> entry : count.entrySet()) {
            String k = entry.getKey();
            Long v = entry.getValue();
            String[] p = k.split("");
            addToMap(p[0], v, totals);
            addToMap(p[1], v, totals);
        }
        addToMap(first, 1L, totals);
        addToMap(last, 1L, totals);

        Long max =0L;
        Long min = 99999999999999L;
        for (Map.Entry<String, Long> entry : totals.entrySet()) {
            String k = entry.getKey();
            Long v = entry.getValue();
            Long t = v/2;
            System.out.println(k + ":" + v/2);
            if (t > max) max = t;
            if (t < min) min = t;
        }
        System.out.println(max-min);

    }

    private static void addToMap(String v, Long n, HashMap<String, Long> map) {
        if (map.containsKey(v)) {
            map.put(v, map.get(v) + n);
        } else {
            map.put(v, n);
        }
    }

    private static void growPolymer() {
        HashMap<String, Long> after = new HashMap<>();
        for (Map.Entry<String, Long> entry : count.entrySet()) {
            String k = entry.getKey();
            Long v = entry.getValue();
            String newValue = returnRuleResult(k);
            String[] p = k.split("");
            addToMap(p[0] + newValue, v, after);
            addToMap(newValue + p[1], v, after);
        }
        count = new HashMap<>();
        count.putAll(after);
    }

    private static String returnRuleResult(String pair) {
        for (String rule : rules) {
            if (rule.contains(pair)) {
                return rule.split("->")[1].trim();
            }
        }
        return "";
    }
}
