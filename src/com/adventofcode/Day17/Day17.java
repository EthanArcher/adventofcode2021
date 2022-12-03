package com.adventofcode.Day17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day17 {

    static int lowX;
    static int highX;
    static int lowY;
    static int highY;
    static int successCount = 0;

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day17/input.txt");
        String input = Files.readAllLines(fileName).get(0);
        String xRange = input.substring(input.indexOf("x=")+2, input.indexOf(","));
        String yRange = input.substring(input.indexOf("y=")+2);
        lowX = Integer.valueOf(xRange.substring(0,xRange.indexOf(".")));
        highX = Integer.valueOf(xRange.substring(xRange.lastIndexOf(".")+1));
        lowY = Integer.valueOf(yRange.substring(0,yRange.indexOf(".")));
        highY = Integer.valueOf(yRange.substring(yRange.lastIndexOf(".")+1));

        int minStartingThrust = getMinStartingThrust();
        int maxStartingVerticalVelocity = getMaxStartingVerticalVelocity();
        System.out.println(minStartingThrust + "," + maxStartingVerticalVelocity);

        for (int thrust=minStartingThrust; thrust<=highX; thrust++) {
            for (int vv=maxStartingVerticalVelocity; vv>=lowY; vv--) {
                wouldLand(thrust, vv);
            }
        }
        System.out.println(successCount);
    }

    private static void wouldLand(int thrust, int velocity) {
        int verticalPosition = 0;
        int horizontalPosition = 0;

        while (verticalPosition > lowY) {
            horizontalPosition+=thrust;
            verticalPosition+=velocity;
            if ((horizontalPosition >= lowX && horizontalPosition <= highX ) && (verticalPosition >= lowY && verticalPosition <= highY )) {
                //System.out.println(initialThrust + "," + initialVV);
                successCount++;
                break;
            }
            if (thrust > 0) thrust--;
            velocity--;
        }
    }

    private static int getMaxStartingVerticalVelocity() {
        int maxVerticalVelocity = lowY+1;
        int maxHeight = 0;
        for (int i=0;i<(-lowY); i++) {
            maxHeight+=i;
        }
        System.out.println("Max height:" + maxHeight);
        return -maxVerticalVelocity;
    }

    private static int getMinStartingThrust() {
        int finalPosition=0;
        int startingThrust = 0;
        int thrust = 0;
        while(startingThrust==0) {
            finalPosition += thrust;
            if (finalPosition >= lowX && finalPosition <= highX ) {
                startingThrust = thrust;
            }
            thrust++;
        }
        return startingThrust;
    }

}
