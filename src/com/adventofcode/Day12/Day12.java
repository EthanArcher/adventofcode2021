package com.adventofcode.Day12;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Day12 {

    private static String route = "";
    private static List<String> paths = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day12/input.txt");
        List<List<String>> actual = Files.readAllLines(fileName).stream()
                .map(line -> Arrays.asList(line.split("-")))
                .collect(Collectors.toList());

        HashMap<String, Cave> caves = new HashMap<>();

        actual.forEach(line -> line.forEach(cave -> caves.put(cave, new Cave(cave))));

        actual.forEach(line -> {
            String from = line.get(0);
            String to = line.get(1);
            caves.get(from).addConnection(to);
            caves.get(to).addConnection(from);
        });

        Cave start = caves.get("start");

        start.getConnections().forEach(c -> {
            String route = "start";
            boolean oneSmallVisitRemaining = true;
            moveThrough(caves, c, route + "," + c, oneSmallVisitRemaining);
        });

        System.out.println(paths.size());

    }

    private static void moveThrough(HashMap<String, Cave> caves, String cave, String route, boolean oneSmallVisitRemaining) {
        List<String> connecting = caves.get(cave).getConnections();
        connecting.forEach(c -> {
            if (c.equals("end")) {
                paths.add(route + "," + c);
            }
            else if (isSmallCave(c) && route.contains(c)) {
                if (oneSmallVisitRemaining && !c.equals("start")) {
                    moveThrough(caves, c, route + "," + c, false);
                } else {
                    // journey contains that cave then we cant go back there
                    // end of route, did not reach end so dont keep it
                }
            } else {
                moveThrough(caves, c, route + "," + c, oneSmallVisitRemaining);
            }
        });

    }

    private static boolean isSmallCave(String cave) {
        return cave.equals(cave.toLowerCase());
    }

    @Getter
    @Setter
    private static class Cave {

        String name;
        List<String> connections;

        public Cave(String name) {
            this.name = name;
            this.connections = new ArrayList<>();
        }

        public void addConnection(String cave) {
            connections.add(cave);
        }

    }
}
