package com.adventofcode.Day4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day4 {

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day4/input.txt");
        List<String> actual = Files.readAllLines(fileName);
        List<Integer> draw = getDraw(actual);

        List<Integer> L1 = lineToListOfInts(actual.get(2).split(" "));

        int numberOfCards = (actual.size() - 1) / 6;

        List<Card> cards = new ArrayList<>();

        for (int i=0; i<numberOfCards;i++) {
            cards.add(getCard(actual, i));
        }
        List<Card> originalValues = new ArrayList<>();
        originalValues.addAll(cards);

        draw.stream().forEach(value -> {
            cards.forEach(card -> card.markValue(value));
            cards.removeIf(card -> checkWinner(card, value) == true);
        });

    }

    private static boolean checkWinner(Card card, int value) {
        for (int i=0; i<card.getValues().size(); i++) {
            if (checkColumn(card, i) || checkRow(card, i)){
                int total = card.findSum() * value;
                System.out.println("winner");
                System.out.println(total);
                return true;
            }
        }
        return false;
    }

    private static Card getCard(List<String> actual, int cardNumber) {
        Card card = new Card(cardNumber);
        for (int i=0; i<5; i++) {
            card.addLine(lineToListOfInts(actual.get((6*cardNumber) + i + 2).split(" ")));
        }
        return card;
    }

    private static boolean checkRow(Card card, int row) {
        int sum=0;
        for (int i=0; i<5; i++) {
            sum+=card.getRowColum(row,i);
        }
        return sum == -5;
    }

    private static boolean checkColumn(Card card, int column) {
        int sum=0;
        for (int i=0; i<5; i++) {
            sum+=card.getRowColum(i,column);
        }
        return sum == -5;
    }

    private static List<Integer> getDraw(List<String> actual) {
        return Arrays.stream(actual.get(0).split(","))
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());
    }

    private static List<Integer> lineToListOfInts(String[] line) {
        return Arrays.stream(line)
                .filter(v -> !v.isEmpty())
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toList());
    }

    private static class Card {

        List<List<Integer>> values;
        int id;

        public Card(int id) {
            values = new ArrayList<>();
            this.id = id;
        }

        public void addLine(List<Integer> line) {
            values.add(line);
        }

        public List<List<Integer>> getValues(){
            return values;
        }

        public int getRowColum(int row, int column){
            return values.get(row).get(column);
        }

        public int getId() {
            return id;
        }

        private void markValue(int value) {
            for (int r=0; r<5; r++) {
                for (int c=0; c<5; c++) {
                    if (values.get(r).get(c) == value) {
                        values.get(r).set(c, -1);
                    }
                }
            }
        }

        private int findSum() {
            int sum = 0;
            for (int r=0; r<5; r++) {
                for (int c=0; c<5; c++) {
                    if (values.get(r).get(c) != -1) {
                        sum += values.get(r).get(c);
                    }
                }
            }
            return sum;
        }
    }


}
