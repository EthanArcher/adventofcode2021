package com.adventofcode.Day20;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Day20 {

    static String[] enhancementAlgorithm;
    static String padString;

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day20/input.txt");
        List<String> input = Files.readAllLines(fileName);

        enhancementAlgorithm  = input.get(0).split("");
        String[][] image = input.subList(2, input.size()).stream()
                .map(line -> line.split(""))
                .collect(Collectors.toList()).toArray(String[][]::new);
        padString = ".";

        String[][] enhanced = image;

        for (int times=0; times<50; times++) {
            enhanced = enhance(enhanced);
        }

        int count = 0;
        for (int x=0; x<enhanced.length; x++) {
            for (int y = 0; y < enhanced[0].length; y++) {
                if (enhanced[x][y].equals("#")) {
                    count++;
                }
            }
        }

        System.out.println(count);

    }

    private static String[][] enhance(String[][] image) {
        String[][] paddedImage = pad(image);
        String[][] doublePad = pad(paddedImage);
        String[][] output = new String[paddedImage.length][paddedImage[0].length];

        for (int x=0; x<paddedImage.length; x++) {
            for (int y=0; y<paddedImage[0].length; y++) {
                String[][] surrounding = new String[3][3];
                for (int i=0; i<3; i++) {
                    for (int j=0; j<3; j++) {
                        surrounding[i][j] = doublePad[x+i][y+j];
                    }
                }
                int value = getValueOfMatrix(surrounding);
                String replacement = enhancementAlgorithm[value];
                output[x][y] = replacement;
            }
        }
        padString = flip(padString);
        return(output);
    }

    private static String flip(String padString) {
        String[][] infinitePad = new String[3][3];
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                infinitePad[i][j] = padString;
            }
        }
        int value = getValueOfMatrix(infinitePad);
        return enhancementAlgorithm[value];
    }

    private static int getValueOfMatrix(String[][] surrounding) {
        StringBuilder builder = new StringBuilder();
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                builder.append(intValueOf(surrounding[i][j]));
            }
        }
        return Integer.valueOf(builder.toString(), 2);
    }

    private static String intValueOf(String s) {
        if (s.equals("#")) {
            return "1";
        } else {
            return "0";
        }
    }

    private static String[][] pad(String[][] image) {
        String[][] paddedImage = new String[image.length+2][image[0].length+2];

        for (int x=0; x<paddedImage.length; x++) {
            for (int y=0; y<paddedImage[0].length; y++) {
                if (x==0 || y==0 || x==paddedImage.length-1 || y==paddedImage[0].length-1) {
                    paddedImage[x][y] = padString;
                } else {
                    paddedImage[x][y] = image[x-1][y-1];
                }
            }
        }

        return paddedImage;
    }


}
