package com.adventofcode.Day22;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day22 {

    static int size = 100;
    static List<Cuboid> storedCuboids = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day22/input.txt");
        List<String> input = Files.readAllLines(fileName);
        for (String line : input) {
            int on = line.contains("on") ? 1 : 0;
            int xIndex = line.indexOf("x");
            int yIndex = line.indexOf("y");
            int zIndex = line.indexOf("z");
            int[] x = fromTo(line.substring(xIndex+2, yIndex-1));
            int[] y = fromTo(line.substring(yIndex+2, zIndex-1));
            int[] z = fromTo(line.substring(zIndex+2));
            updateListOfCuboids(new Cuboid(x,y,z), on);
        }
        long count=totalArea(storedCuboids);
        System.out.println(count);
        if (count == 1235164413198198L) {
            System.out.println("still working");
        } else System.out.println("that broke it");

    }

    /**
     *
     * @param incoming - this is the new cuboid we want to add
     * @param on - this is a flag if we are turning the new cuboid on or off
     * @return
     */
    private static void updateListOfCuboids(Cuboid incoming, int on) {

        int[] x = incoming.getX();
        int[] y = incoming.getY();
        int[] z = incoming.getZ();

        if (storedCuboids.isEmpty()) {
            storedCuboids.add(new Cuboid(x,y,z));
        } else {
            List<Cuboid> originals = new ArrayList<>();
            originals.addAll(storedCuboids);
            // check each of the original stored cubes
            // if this cuboid intersects with the new cuboid being added
            // then remove this cuboid from the final set
            // replace it with the split version of it
            for (int i=0; i<originals.size(); i++) {
                Cuboid cuboid = originals.get(i);
                if (intercepts(x, y, z, cuboid)) {
                    storedCuboids.remove(cuboid);

                    int leftIntercept = Math.max(cuboid.getX()[0], x[0]);
                    int rightIntercept = Math.min(cuboid.getX()[1], x[1]);
                    int downIntercept = Math.max(cuboid.getY()[0], y[0]);
                    int upIntercept = Math.min(cuboid.getY()[1], y[1]);
                    int backwardIntercept = Math.max(cuboid.getZ()[0], z[0]);
                    int forwardIntercept = Math.min(cuboid.getZ()[1], z[1]);

                    // on the left
                    if (leftIntercept > cuboid.getX()[0]) {
                        storedCuboids.add(new Cuboid(new int[]{cuboid.getX()[0], leftIntercept-1}, cuboid.getY(), cuboid.getZ()));
                    }
                    // whats on the right
                    if (rightIntercept < cuboid.getX()[1]) {
                        storedCuboids.add(new Cuboid(new int[]{rightIntercept+1, cuboid.getX()[1]}, cuboid.getY(), cuboid.getZ()));
                    }
                    // everything below between intersections of left and right
                    if (downIntercept > cuboid.getY()[0]) {
                        storedCuboids.add(new Cuboid(new int[]{leftIntercept, rightIntercept}, new int[]{cuboid.getY()[0], downIntercept-1}, cuboid.getZ()));
                    }
                    // everything above between intersections of left and right
                    if (upIntercept < cuboid.getY()[1]) {
                        storedCuboids.add(new Cuboid(new int[]{leftIntercept, rightIntercept}, new int[]{upIntercept+1, cuboid.getY()[1]}, cuboid.getZ()));
                    }
                    // everything backwards inside x and y and inside down and up
                    if (backwardIntercept > cuboid.getZ()[0]) {
                        storedCuboids.add(new Cuboid(new int[]{leftIntercept, rightIntercept}, new int[]{downIntercept, upIntercept}, new int[]{cuboid.getZ()[0], backwardIntercept-1}));
                    }
                    // everything forwards inside x and y and inside down and up
                    if (forwardIntercept < cuboid.getZ()[1]) {
                        storedCuboids.add(new Cuboid(new int[]{leftIntercept, rightIntercept}, new int[]{downIntercept, upIntercept}, new int[]{forwardIntercept+1, cuboid.getZ()[1]}));
                    }
                }
            }
            // if we are turning on, then we add the new cuboid, otherwise we dont need it
            if (on == 1) {
                storedCuboids.add(new Cuboid(x, y, z));
            }
        }

    }

    private static long totalArea(List<Cuboid> cuboids) {
        long area = 0;
        for (Cuboid cuboid : cuboids) {
            area+= (long) (cuboid.getX()[1] + 1 - cuboid.getX()[0]) * (cuboid.getY()[1] + 1 - cuboid.getY()[0]) * (cuboid.getZ()[1] + 1 - cuboid.getZ()[0]);
        }
        return area;
    }

    private static boolean intercepts(int x[], int y[], int z[], Cuboid cuboid) {
        return interceptsPlane(x, cuboid.getX()) && interceptsPlane(y, cuboid.getY()) && interceptsPlane(z, cuboid.getZ());
    }

    private static boolean interceptsPlane(int[] v1, int[] v2) {
        return (v1[0] >= v2[0] && v1[0] <= v2[1]) || (v1[1] >= v2[0] && v1[1] <= v2[1]) || (v1[0] <= v2[0] && v1[1] >= v2[1]);
    }

    private static int[] fromTo(String values) {
        return new int[]{
                Integer.valueOf(values.substring(0, values.indexOf("."))),
                Integer.valueOf(values.substring(values.lastIndexOf(".") + 1))};
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private static class Cuboid{
        int[] x;
        int[] y;
        int[] z;
    }
}