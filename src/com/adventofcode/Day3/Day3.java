package com.adventofcode.Day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day3 {

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day3/input.txt");
        List<String> actual = Files.readAllLines(fileName);

        List<int[]> binarys = actual.stream()
                .map(line -> stringToIntArray(line))
                .collect(Collectors.toList());

        partA(binarys);
        partB(binarys);

    }

    private static void partA(List<int[]> binarys) {
        int arraySize = binarys.get(0).length;

        int[] mcv = getMostCommonValues(binarys);
        int[] gamma = new int[arraySize];
        int[] epsilon = new int[arraySize];
        for (int i=0; i<mcv.length; i++) {
            if (mcv[i] == 1 ) {
                gamma[i] = 1;
                epsilon[i] = 0;
            } else {
                gamma[i] = 0;
                epsilon[i] = 1;
            }
        }

        String gammaString = toString(gamma);
        String epsilonString = toString(epsilon);

        int powerConsumpltion = Integer.valueOf(gammaString,2) * Integer.valueOf(epsilonString,2);
        System.out.println(powerConsumpltion);
    }

    private static void partB(List<int[]> binarys) {
        int oxygenGeneratorRating = getOxygenGeneratorRating(binarys);
        int co2Rating = getCO2Rating(binarys);
        int lifeSupportRating = oxygenGeneratorRating * co2Rating;
        System.out.println(lifeSupportRating);
    }

    private static int getCO2Rating(List<int[]> binarys) {
        int arraySize = binarys.get(0).length;

        List<int[]> binarysForCO2 = new ArrayList<>(binarys);
        for (int i=0; i<arraySize; i++) {
            int removeIndex = i;
            if (binarysForCO2.size() > 1) {
                int[] mcv = getMostCommonValues(binarysForCO2);
                binarysForCO2.removeIf(values -> values[removeIndex] == mcv[removeIndex]);
            }
        }
        return Integer.valueOf(toString(binarysForCO2.get(0)),2);

    }

    private static int getOxygenGeneratorRating(List<int[]> binarys) {
        int arraySize = binarys.get(0).length;

        List<int[]> binarysForOxygen = new ArrayList<>(binarys);
        for (int i=0; i<arraySize; i++) {
            int removeIndex = i;
            if (binarysForOxygen.size() > 1) {
                int[] mcv = getMostCommonValues(binarysForOxygen);
                binarysForOxygen.removeIf(values -> values[removeIndex] != mcv[removeIndex]);
            }
        }
        return Integer.valueOf(toString(binarysForOxygen.get(0)),2);
    }

    private static int[] stringToIntArray(String line) {
        String[] s = line.split("");
        return Arrays.stream(s).mapToInt(Integer::parseInt).toArray();
    }

    private static int[] getMostCommonValues(List<int[]> binarys) {
        int arraySize = binarys.get(0).length;

        int[] totals = new int[arraySize];

        binarys.forEach(values -> {
            for (int i=0; i<values.length; i++) {
                totals[i]+= values[i];
            }
        });

        int[] mcv = new int[arraySize];
        double half = (double) binarys.size()/2;
        for (int i=0; i<totals.length; i++) {
            if (totals[i] >= half) {
                mcv[i] = 1;
            } else {
                mcv[i] = 0;
            }
        }
        return mcv;
    }

    private static String toString(int[] totals) {
        StringBuilder builder = new StringBuilder();
        for(int s : totals) {
            builder.append(s);
        }
        return builder.toString();
    }

}
