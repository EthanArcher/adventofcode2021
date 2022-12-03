package com.adventofcode.Day18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day18 {

    private static final int LEFT = -1;
    private static final int RIGHT = 1;

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day18/input.txt");
        List<String> snailFish = Files.readAllLines(fileName);

        List<ValueAndDepth> valueAndDepths = new ArrayList<>();
        snailFish.forEach(sf -> {
            combine(valueAndDepths, sf);
            doTheThing(valueAndDepths);
        });

        int magnitude = getMagnitude(valueAndDepths);
        System.out.println(magnitude);

        int maxMagnitude = 0;
        for (int index1=0; index1<snailFish.size()-1; index1++) {
            for (int index2=index1+1; index2<snailFish.size()-1; index2++) {
                int mag = 0;
                mag = getMag(snailFish, valueAndDepths, index1, index2);
                maxMagnitude = Math.max(maxMagnitude, mag);
                mag = getMag(snailFish, valueAndDepths, index2, index1);
                maxMagnitude = Math.max(maxMagnitude, mag);
            }
        }

        System.out.println(maxMagnitude);

    }

    private static int getMag(List<String> snailFish, List<ValueAndDepth> valueAndDepths, int index1, int index2) {
        List<ValueAndDepth> vd2 = new ArrayList<>();
        combine(vd2, snailFish.get(index1));
        combine(vd2, snailFish.get(index2));
        doTheThing(vd2);
        return getMagnitude(vd2);
    }

    private static int getMagnitude(List<ValueAndDepth> valueAndDepths) {
        int deep = 4;
        while (deep>0) {
            for (int index=0; index<valueAndDepths.size()-1; index++) {
                ValueAndDepth vdL = valueAndDepths.get(index);
                ValueAndDepth vdR = valueAndDepths.get(index+1);
                if (deep == vdL.getDepth() && deep == vdR.getDepth()) {
                    int depth = vdL.getDepth()-1;
                    int mag = getMagnitudeFromPair(vdL.getValue(), vdR.getValue());
                    valueAndDepths.remove(index);
                    valueAndDepths.remove(index);
                    valueAndDepths.add(index, new ValueAndDepth(mag, depth));
                }
            }
            deep--;
        }
        return valueAndDepths.get(0).getValue();
    }

    private static int getMagnitudeFromPair(int l, int r) {
        return 3*l + 2*r;
    }


    private static void doTheThing(List<ValueAndDepth> valueAndDepths) {
        if (valueAndDepths.stream().anyMatch(vd -> vd.getDepth()>4)) {
            explode(valueAndDepths);
            doTheThing(valueAndDepths);
        } else if (valueAndDepths.stream().anyMatch(vd -> vd.getValue()>=10)) {
            split(valueAndDepths);
            doTheThing(valueAndDepths);
        }
    }

    private static List<ValueAndDepth> stringToValueAndDepths(String s) {
        List<ValueAndDepth> valueAndDepths = new ArrayList<>();
        int depth = 0;
        for (String c : s.split("")) {
            if (c.equals("[")) {
                depth++;
            } else if (c.equals("]")) {
                depth--;
            } else if (c.equals(",")) {

            } else {
                valueAndDepths.add(new ValueAndDepth(Integer.valueOf(c), depth));
            }
        }
        return valueAndDepths;
    }

    private static void explode(List<ValueAndDepth> valueAndDepths) {
        for (int index=0; index<valueAndDepths.size(); index++) {
            ValueAndDepth vd = valueAndDepths.get(index);
            if (vd.getDepth() > 4) {
                incrementValue(valueAndDepths, index, LEFT);
                valueAndDepths.remove(index);
                incrementValue(valueAndDepths, index, RIGHT);
                valueAndDepths.remove(index);
                valueAndDepths.add(index, new ValueAndDepth(0, 4));
                break;
            }
        }
    }

    private static void split(List<ValueAndDepth> valueAndDepths) {
        for (int index=0; index<valueAndDepths.size(); index++) {
            ValueAndDepth vd = valueAndDepths.get(index);
            if (vd.getValue() >= 10) {
                double v = (double)vd.getValue()/2;
                valueAndDepths.remove(index);
                valueAndDepths.add(index, new ValueAndDepth((int) Math.floor(v), vd.getDepth()+1));
                valueAndDepths.add(index+1, new ValueAndDepth((int) Math.ceil(v), vd.getDepth()+1));
                break;
            }
        }

    }

    private static void incrementValue(List<ValueAndDepth> valueAndDepths, int index, int direction) {
        int newIndex = index + direction;
        ValueAndDepth v = valueAndDepths.get(index);
        if (newIndex>=0 && newIndex< valueAndDepths.size()) {
            ValueAndDepth x = valueAndDepths.get(newIndex);
            x.incrementValueBy(v.getValue());
            valueAndDepths.set(newIndex, x);
        }
    }

    private static void combine(List<ValueAndDepth> valueAndDepths, String additions) {
        if (valueAndDepths.isEmpty()) {
            valueAndDepths.addAll(stringToValueAndDepths(additions));
        } else {
            valueAndDepths.addAll(stringToValueAndDepths(additions));
            valueAndDepths.forEach(vd -> vd.setDepth(vd.getDepth()+1));
        }
    }

    private static class ValueAndDepth {
        private int value;
        private int depth;

        public ValueAndDepth(int value, int depth){
            this.value = value;
            this.depth = depth;
        }
        public int getValue() {
            return value;
        }
        public int getDepth() {
            return depth;
        }
        public void setDepth(int depth) {
            this.depth = depth;
        }
        public void setValue(int value) {
            this.value = value;
        }
        public void incrementValueBy(int v) {
            value+=v;
        }
        public String toString() {
            return value + " at " + depth;
        }
        public void decreaseDepth() {
            depth--;
        }
    }


}
