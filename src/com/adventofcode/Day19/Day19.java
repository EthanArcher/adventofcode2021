package com.adventofcode.Day19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Day19 {

    static List<List<Beacon>> beaconList = new ArrayList<>();
    static List<Beacon> scannerPositions = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day19/input.txt");
        List<String> input = Files.readAllLines(fileName);
        List<Scanner> scanners = parseInput(input);
        System.out.println(scanners);

        beaconList.add(scanners.get(0).getBeacons());
        scannerPositions.add(new Beacon(0,0,0));

        HashMap<Integer, Boolean> matched = new HashMap<>();
        for (int i=1; i<scanners.size(); i++) {
            matched.put(i, false);
        }

        // until all of the values are matched
        while(matched.containsValue(false)) {
            for (Map.Entry<Integer, Boolean> entry : matched.entrySet()) {
                System.out.println(matched);
                Integer id = entry.getKey();
                Boolean matchedAlready = entry.getValue();
                if (!matchedAlready) {
                    Scanner scanner = scanners.get(id);
                    // for each set of beacons in the list,
                    // this keeps the co-ordinates in their original sets so they can only match the correct 12 times
                    for (int i=0; i<beaconList.size(); i++) {
                        boolean match = findMatchingBeacons(beaconList.get(i), scanner);
                        if (match) {
                            matched.replace(id, true);
                            break;
                        }
                    }
                }
            }
        }

        // this is not ideal but it works by checking the coordinates as a String
        Set<String> unique = new LinkedHashSet<>();
        List<Beacon> flat = beaconList.stream().flatMap(List::stream).collect(Collectors.toList());

        for (Beacon beacon : flat) {
            unique.add(beacon.toString());
        }
        System.out.println(unique.size());

        // the Manhattan distance is |x1-x2| + |y1-y2| + |z1-z2|
        System.out.println(scannerPositions);
        int maxManhatten = 0;
        for (int outer = 0; outer<scannerPositions.size(); outer++) {
            for (int inner = 1; inner<scannerPositions.size(); inner++) {
                maxManhatten = Math.max(maxManhatten, getManhatten(scannerPositions.get(outer), scannerPositions.get(inner)));
            }
        }
        System.out.println(maxManhatten);
    }

    private static int getManhatten(Beacon a, Beacon b) {
        return Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY()) + Math.abs(a.getZ() - b.getZ());
    }

    private static boolean findMatchingBeacons(List<Beacon> beaconList, Scanner scanner1) {
        List<Beacon> XYZ = scanner1.getBeacons();
        List<Beacon> XZY = scanner1.getBeacons().stream().map(beacon -> new Beacon(beacon.getX(), beacon.getZ(), beacon.getY())).collect(Collectors.toList());
        List<Beacon> YXZ = scanner1.getBeacons().stream().map(beacon -> new Beacon(beacon.getY(), beacon.getX(), beacon.getZ())).collect(Collectors.toList());
        List<Beacon> YZX = scanner1.getBeacons().stream().map(beacon -> new Beacon(beacon.getY(), beacon.getZ(), beacon.getX())).collect(Collectors.toList());
        List<Beacon> ZYX = scanner1.getBeacons().stream().map(beacon -> new Beacon(beacon.getZ(), beacon.getY(), beacon.getX())).collect(Collectors.toList());
        List<Beacon> ZXY = scanner1.getBeacons().stream().map(beacon -> new Beacon(beacon.getZ(), beacon.getX(), beacon.getY())).collect(Collectors.toList());

        // this method ends up checking all 48 versions, half of these live in the mirror realm (RHR) but it works so not fixing for now
        boolean a = rotations(beaconList, XYZ);
        boolean b = rotations(beaconList, XZY);
        boolean c = rotations(beaconList, YXZ);
        boolean d = rotations(beaconList, YZX);
        boolean e = rotations(beaconList, ZYX);
        boolean f = rotations(beaconList, ZXY);

        return a || b || c || d || e || f;
    }

    private static boolean rotations(List<Beacon> beacons0, List<Beacon> beacons1) {
        HashMap<String, Integer> possibleScannerPositions;

        //triple nested for checks for positive and negative of each version
        for (int x=0; x<=1;x++) {
            for (int y=0; y<=1;y++) {
                for (int z=0; z<=1;z++) {
                    possibleScannerPositions= new HashMap<>();
                    List<Beacon> rotatedBeaconPositions = getBeaconsWithRotation(beacons1, x, y, z);
                    for (int index0=0; index0<beacons0.size(); index0++) {
                        for (int index1 = 0; index1 < rotatedBeaconPositions.size(); index1++) {
                            Beacon resultingPositionBeacon = getResultingPosition(beacons0.get(index0), rotatedBeaconPositions.get(index1));
                            addToMap(resultingPositionBeacon, possibleScannerPositions);
                        }
                    }
                    Map.Entry<String, Integer> entry = possibleScannerPositions.entrySet().stream()
                            .sorted((k1, k2) -> -(k1.getValue().compareTo(k2.getValue())))
                            .findFirst().get();
                    //System.out.println(entry.getKey() + " : " + entry.getValue() );
                    if (entry.getValue() >= 12) {
                        scannerPositions.add(new Beacon(entry.getKey()));
                        List<Beacon> fromOrigin = rotatedBeaconPositions.stream()
                                .map(beacon -> getBeaconFromOrigin(new Beacon(entry.getKey()), beacon))
                                .collect(Collectors.toList());
                        beaconList.add(fromOrigin);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static void addToMap(Beacon resultingPositionBeacon, HashMap<String, Integer> possibleScannerPositions) {
        String coordinates = resultingPositionBeacon.toString();
        if (possibleScannerPositions.containsKey(coordinates)) {
            possibleScannerPositions.put(coordinates, possibleScannerPositions.get(coordinates) + 1);
        } else {
            possibleScannerPositions.put(coordinates, 1);
        }
    }

    private static List<Beacon> getBeaconsWithRotation(List<Beacon> beacons1, int x, int y, int z) {
        return beacons1.stream()
                .map(b -> b.rotate(x,y,z))
                .collect(Collectors.toList());
    }

    private static Beacon getResultingPosition(Beacon anyBeacon0, Beacon anyBeacon1) {
        return new Beacon(anyBeacon0.getX() - anyBeacon1.getX(), anyBeacon0.getY() - anyBeacon1.getY(), anyBeacon0.getZ() - anyBeacon1.getZ());
    }

    private static Beacon getBeaconFromOrigin(Beacon anyBeacon0, Beacon anyBeacon1) {
        return new Beacon(anyBeacon0.getX() + anyBeacon1.getX(), anyBeacon0.getY() + anyBeacon1.getY(), anyBeacon0.getZ() + anyBeacon1.getZ());
    }

    private static List<Scanner> parseInput(List<String> input) {
        List<Scanner> scanners = new ArrayList<>();
        Scanner scanner = new Scanner(0);
        for (String line : input) {
            if (line.contains("--- scanner")) {
                scanner = new Scanner(Integer.valueOf(line.substring(12, line.length()-4)));
            } else if (!line.isBlank()) {
                scanner.addBeacon(new Beacon(line));
            } else {
                scanners.add(scanner);
            }
        }
        scanners.add(scanner);
        return scanners;

    }

    private static class Scanner  {
        private List<Beacon> beacons;
        private int id;

        public Scanner(int id) {
            this.id = id;
            this.beacons = new ArrayList<>();
        }

        public void addBeacon(Beacon beacon) {
            beacons.add(beacon);
        }

        public List<Beacon> getBeacons() {
            return beacons;
        }
    }

    public static class Beacon {
        private int x, y, z;

        public Beacon(int x, int y, int z) {
            this.x=x;
            this.y=y;
            this.z=z;
        }

        public Beacon(String line) {
            String[] l = line.split(",");
            this.x=Integer.valueOf(l[0]);
            this.y=Integer.valueOf(l[1]);
            this.z=Integer.valueOf(l[2]);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        @Override
        public String toString() {
            return x + "," + y + "," + z;
        }

        public Beacon rotate(int X, int Y, int Z) {
            int vX = X==0 ? 1 : -1;
            int vY = Y==0 ? 1 : -1;
            int vZ = Z==0 ? 1 : -1;
            return new Beacon(x*vX, y*vY, z*vZ);
        }
    }


}
