package com.adventofcode.Day16;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day16 {

    static List<Integer> versions = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day16/input.txt");
        String input = Files.readAllLines(fileName).stream()
                .map(line -> hexStringToBinaryString(line))
                .collect(Collectors.toList()).get(0);

        System.out.println(input);

        interpretBinary(input);

        int sum = versions.stream().reduce(0, Integer::sum);
        System.out.println(sum);

    }

    /******
     * A recursive function which will split the binaryInput into each smaller binary input
     * Returning the subtotal for the binaryInput from that packet
     * @param binaryInput
     * @return ValueAndRemaining containing the current value and the remaining binary input yet to be processed
     */

    private static ValueAndRemaining interpretBinary(String binaryInput) {
        int version = (int) binaryToDecimal(binaryInput.substring(0,3));
        int type = (int) binaryToDecimal(binaryInput.substring(3,6));
        String packets = binaryInput.substring(6);

        versions.add((version));

        if (type == 4) {
            // reached a value, compute value and return it ignoring any trailing 0's
            ValueAndRemaining v = packetToBinary(packets);
            System.out.println(v.getValue());
            return v;
        } else {
            // still got some packets, determine what to do
            System.out.println(packets);
            int id = Integer.valueOf(packets.substring(0,1));
            List<Long> subTotal = new ArrayList<>();
            if (id == 0) {
                int bits = 15;
                long lengthOfPacket = binaryToDecimal(packets.substring(1,bits+1));
                int end = (int) (bits+lengthOfPacket+1);
                String subPackets = packets.substring(bits+1,end);
                String remaining = packets.substring(end);

                ValueAndRemaining v = new ValueAndRemaining(0, subPackets);
                do {
                    v = interpretBinary(v.getRemaining());
                    subTotal.add(v.getValue());
                } while (!v.getRemaining().isEmpty());

                System.out.println(subTotal);
                Long res = calculate(subTotal, type);
                System.out.println("Result: " + res);
                // return the subtotal for this packet and any remaining packets
                return new ValueAndRemaining(res, remaining);
            } else {
                int bits = 11;
                long numberOfPackets = binaryToDecimal(packets.substring(1,bits+1));
                String subPackets = packets.substring(bits+1);

                ValueAndRemaining v = new ValueAndRemaining(0, subPackets);
                int counter = 0;
                do {
                    v = interpretBinary(v.getRemaining());
                    subTotal.add(v.getValue());
                    counter++;
                } while (counter<numberOfPackets);

                System.out.println(subTotal);
                Long res = calculate(subTotal, type);
                System.out.println("Result: " + res);
                // return the subtotal for this packet and any remaining packets
                return new ValueAndRemaining(res, v.getRemaining());
            }
        }
    }

    private static Long calculate(List<Long> subTotal, int type) {
        switch(type) {
            case 0:
                return subTotal.stream().reduce(0L, Long::sum);
            case 1:
                return subTotal.stream().reduce(1L, (a,b) -> a*b);
            case 2:
                return subTotal.stream().min(Long::compare).get();
            case 3:
                return subTotal.stream().max(Long::compare).get();
            case 5:
                return subTotal.get(0) > subTotal.get(1) ? 1L : 0L;
            case 6:
                return subTotal.get(0) < subTotal.get(1) ? 1L : 0L;
            case 7:
                return subTotal.get(0) == subTotal.get(1) ? 1L : 0L;
            default:
                return 0L;
        }
    }

    private static ValueAndRemaining packetToBinary(String p) {
        List<String> packets = new ArrayList<>();
        for (int i=0; i<(p.length()/5)*5; i+=5) {
            packets.add(p.substring(i,i+5));
        }
        StringBuilder packs = new StringBuilder();
        int position=0;
        for (String pk : packets) {
            position+=5;
            packs.append(pk, 1, 5);
            if (Integer.valueOf(pk.substring(0, 1)) == 0) {
                return new ValueAndRemaining(binaryToDecimal(packs.toString()), p.substring(position));
            }
        }
        System.out.println("Error should never get here");
        return new ValueAndRemaining(0, "");
    }

    private static String hexStringToBinaryString(String hex) {
        hex = hex.replaceAll("0", "0000");
        hex = hex.replaceAll("1", "0001");
        hex = hex.replaceAll("2", "0010");
        hex = hex.replaceAll("3", "0011");
        hex = hex.replaceAll("4", "0100");
        hex = hex.replaceAll("5", "0101");
        hex = hex.replaceAll("6", "0110");
        hex = hex.replaceAll("7", "0111");
        hex = hex.replaceAll("8", "1000");
        hex = hex.replaceAll("9", "1001");
        hex = hex.replaceAll("A", "1010");
        hex = hex.replaceAll("B", "1011");
        hex = hex.replaceAll("C", "1100");
        hex = hex.replaceAll("D", "1101");
        hex = hex.replaceAll("E", "1110");
        hex = hex.replaceAll("F", "1111");
        return hex;
    }

    private static long binaryToDecimal(String binary) {
        return new BigInteger(binary, 2).longValue();
    }

    private static class ValueAndRemaining {
        private long value;
        private String remaining;

        public ValueAndRemaining(long value, String remaining){
            this.value=value;
            this.remaining=remaining;
        }

        public String getRemaining() {
            return remaining;
        }

        public long getValue() {
            return value;
        }
    }

}
