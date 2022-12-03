package com.adventofcode.Day21;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Day21 {

    static int dice;
    static List<Player> players = new ArrayList<>();
    static HashMap<String, Wins> cache = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day21/input.txt");
        List<String> input = Files.readAllLines(fileName);

        for (int index=0; index<input.size(); index++) {
            players.add(new Player(Integer.valueOf(input.get(index).split(":")[1].trim()), 0));
        }

        //partA();
        partB();

    }

    private static void partB() {
        Wins wins = takeQuantumTurn(new Input(
                0,
                players.get(0).getPosition(),
                players.get(0).getScore(),
                players.get(1).getPosition(),
                players.get(1).getScore()));

        // position, count
        System.out.println(Math.max(wins.getP1(), wins.getP2()));
        System.out.println(cache.size());

    }

    private static Wins takeQuantumTurn(Input input) {
        int player = input.getPlayer();
        int pos = input.getPos();
        int score = input.getScore();

        if (cache.containsKey(input.toString())) {
            return cache.get(input.toString());
        } else {
            Wins wins = new Wins(0L,0L);
            for (int i = 1; i <= 3; i++) {
                for (int j = 1; j <= 3; j++) {
                    for (int k = 1; k <= 3; k++) {
                        int roll = i + j + k;
                        int newPos = getNewBoardPosition(pos, roll);
                        int newScore = score + newPos;
                        Wins res;
                        if (newScore>=21) {
                            if (player == 0) {
                                wins.setP1(wins.getP1() + 1);
                            } else {
                                wins.setP2(wins.getP2() + 1);
                            }
                        } else {
                            res = takeQuantumTurn(new Input(1 - player, input.getOppPos(), input.getOppScore(), newPos, newScore));
                            wins.setP1(wins.getP1() + res.getP1());
                            wins.setP2(wins.getP2() + res.getP2());
                        }
                    cache.put(input.toString(), wins);
                    }
                }
            }
            return wins;
        }
    }

    private static void partA() {
        int rolls=0;
        int player = 0;
        while((players.get(0).getScore()<1000) && (players.get(1).getScore()<1000)) {
            rolls+=3;
            takeTurn(players.get(player));
            player = (player+1) % 2;
        }

        System.out.println(players.get(0));
        System.out.println(players.get(1));
        System.out.println(rolls);

        if (players.get(0).getScore()<1000) {
            System.out.println(players.get(0).getScore()*rolls);
        } else {
            System.out.println(players.get(1).getScore()*rolls);
        }
    }

    private static void takeTurn(Player player) {
        for (int i=0; i<3;i++) {
            int roll = rollDice();
            player.setPosition(getNewBoardPosition(player.getPosition(), roll));
        }
        player.setScore(player.getScore() + player.getPosition());
    }

    private static int getNewBoardPosition(int currentPosition, int roll) {
        currentPosition = (currentPosition + roll) % 10;
        if (currentPosition==0) {
            currentPosition = 10;
        }
        return currentPosition;
    }

    private static int rollDice() {
        dice++;
        if (dice>100) dice=1;
        return dice;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    private static class Wins {
        Long p1;
        Long p2;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    private static class Player {
        int position;
        int score;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @ToString
    static class Input {
        int player;
        int pos;
        int score;
        int oppPos;
        int oppScore;
    }
}
