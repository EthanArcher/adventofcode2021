package com.adventofcode.Day23;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Day23 {

    static Amphipod[] corridor = new Amphipod[11];
    static HashMap<String, State> positions = new HashMap<>();
    static List<Integer> completedCosts = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of("src/com/adventofcode/Day23/input.txt");
        List<String> input = Files.readAllLines(fileName);

        for (int i=0; i<10; i++) {
            corridor[i] = null;
        }

        Amphipod[][] rooms = new Amphipod[4][2];
        int d = 0;
        for (String line : input) {
            List<String> vals = Arrays.stream(line.split("#"))
                    .filter(v -> !v.isBlank())
                    .filter(v -> !v.equals("."))
                    .collect(Collectors.toList());
            if (vals.size()==4) {
                rooms[0][d] = new Amphipod(vals.get(0), 2);
                rooms[1][d] = new Amphipod(vals.get(1), 4);
                rooms[2][d] = new Amphipod(vals.get(2), 6);
                rooms[3][d] = new Amphipod(vals.get(3), 8);
                d++;
            }
        }

        State current = new State(0, corridor, rooms);
        addNewStateIfLowerCost(current);

        while(positions.size() != 0) {
            System.out.println(positions.size());
            HashMap<String, State> positionsCopy = new HashMap<>();
            positionsCopy.putAll(positions);
            positionsCopy.values().forEach(p -> {
                makeAMove(p);
            });
        }

        System.out.println(positions);


    }

    private static void makeAMove(State current) {

        Amphipod[][] rooms = current.getRooms();
        Amphipod[] corridor = current.getCorridor();
        boolean sorted = true;
        for (int r=0; r<4; r++) {
            for (Amphipod amphipod : rooms[r]) {
                if (amphipod != null) {
                    sorted = sorted && amphipod.getType().equals(roomToType(r));
                } else {
                    sorted = false;
                }
            }
        }
        for (int c=0; c<10; c++) {
            if (corridor[c] != null) {
                sorted = false;
            }
        }
        if (sorted) {
            // all rooms are sorted
            // stop processing
            System.out.println(current);
            completedCosts.add(current.getCost());
            positions.remove(current.status());
        } else {
            // check if anyone can go home
            boolean someoneWentHome = false;
            for (int i = 0; i < corridor.length; i++) {
                Amphipod corridorSpace = corridor[i];
                // if there is something in that corridor position
                if (corridorSpace != null) {
                    // if there is space in the correct room for this Amphipod, then put them in
                    int targetRoom = typeToRoom(corridorSpace.getType());
                    if (rooms[targetRoom][0] == null) {
                        // if the path to the room is clear
                        int destination = (targetRoom+1) * 2;
                        int dir = i < destination ? 1 : -1;
                        if (pathToRoomClear(corridor, i, destination, dir)) {
                            // move the piece to the room
                            movePieceToRoom(current, rooms, corridor, corridorSpace, i, targetRoom);
                            positions.remove(current.status());
                            someoneWentHome = true;
                            break;
                        }
                    }
                }
            }

            if (!someoneWentHome) {
                // if there is anything in the rooms
                for (int roomNumber = 0; roomNumber < 4; roomNumber++) {
                    Amphipod[] room = rooms[roomNumber];
                    Amphipod top = room[0];
                    Amphipod bottom = room[1];
                    // if there is something in the room that shouldn't be
                    if (top != null) {
                        // move the top piece
                        int corridorPos = (roomNumber + 1) * 2;
                        Amphipod[][] updatedRooms = Arrays.copyOf(rooms, 4);
                        Amphipod[] updatedRoom = Arrays.copyOf(room, 2);
                        int cost;
                        // if there is a bottom piece that is in the right room
                        if (bottom != null && bottom.getType().equals(roomToType(roomNumber))) {
                            updatedRoom[0] = null;
                            cost = current.getCost() + top.getCost();
                        } else {
                            updatedRoom[0] = bottom;
                            updatedRoom[1] = null;
                            int bottomUpCost = bottom!=null ? bottom.getCost() : 0;
                            cost = current.getCost() + top.getCost() + bottomUpCost;
                        }
                        updatedRooms[roomNumber] = updatedRoom;
                        State updated = new State(cost, corridor, updatedRooms);
                        // if we can move left put it there
                        if (corridor[corridorPos - 1] == null) {
                            // try for every available position on the left
                            movePieceFromRoom(updated, updatedRooms, corridor, top, corridorPos, -1);
                        }
                        if (corridor[corridorPos + 1] == null) {
                            // try for every available position on the left
                            movePieceFromRoom(updated, updatedRooms, corridor, top, corridorPos, 1);
                        }
                    }
                    positions.remove(current.status());
                }
            }
        }
    }

    private static boolean pathToRoomClear(Amphipod[] corridor, int corridorPosition, int destination, int dir) {
        boolean clear = true;
        for(int p=corridorPosition + dir; p!=destination; p=p+dir) {
            clear = clear && (corridor[p] == null);
        }
        return clear;
    }

    private static void movePieceFromRoom(State current, Amphipod[][] rooms, Amphipod[] corridor, Amphipod amphipod, int corridorPosition, int direction) {
        int newCorridorPosition = corridorPosition + direction;
        State updated;
        Amphipod[] updatedCorridor;

        int cost = current.getCost();

        while(newCorridorPosition >= 0 && newCorridorPosition<= 10) {
            cost = cost + amphipod.getCost();
            if (isUsableCorridorSpace(newCorridorPosition)) {
                if (corridor[newCorridorPosition] == null) {
                    updatedCorridor = Arrays.copyOf(corridor, 11);
                    updatedCorridor[newCorridorPosition] = amphipod;
                    updated = new State(cost, updatedCorridor, rooms);
                    addNewStateIfLowerCost(updated);
                    //System.out.println(updated.toString());
                } else {
                    break;
                }
            }
            newCorridorPosition = newCorridorPosition + direction;
        }
    }

    private static void movePieceToRoom(State current, Amphipod[][] rooms, Amphipod[] corridor, Amphipod amphipod, int corridorPosition, int destinationRoom) {
        State updated;
        Amphipod[] updatedCorridor = Arrays.copyOf(corridor, 11);
        Amphipod[][] updatedRooms = Arrays.copyOf(rooms, 4);
        Amphipod[] updatedRoom = Arrays.copyOf(rooms[destinationRoom], 2);
        int destination = (destinationRoom+1) * 2;
        int travel = Math.abs(corridorPosition - destination) + 1;

        updatedCorridor[corridorPosition] = null;
        updatedRoom[0] = amphipod;
        updatedRooms[destinationRoom] = updatedRoom;
        int cost = current.getCost() + (amphipod.getCost() * travel);

        if (updatedRoom[1] == null) {
            updatedRoom[1] = amphipod;
            updatedRoom[0] = null;
            cost = cost + amphipod.getCost();
        }

        updated = new State(cost, updatedCorridor, updatedRooms);
        addNewStateIfLowerCost(updated);
    }

    private static boolean isUsableCorridorSpace(int newCorridorPosition) {
        return newCorridorPosition!=2 &&  newCorridorPosition!=4 && newCorridorPosition!=6 && newCorridorPosition!=8;
    }

    private static void addNewStateIfLowerCost(State state) {
        if (positions.containsKey(state.status())) {
            if (positions.get(state.status()).getCost() < state.getCost()) {
                positions.replace(state.status(), state);
            }
        } else {
            positions.put(state.status(), state);
        }
    }

    private static String roomToType(int room) {
        switch(room) {
            case 0:
                return "A";
            case 1:
                return "B";
            case 2:
                return "C";
            case 3:
                return "D";
        }
        return "";
    }

    private static int typeToRoom(String type) {
        switch(type) {
            case "A":
                return 0;
            case "B":
                return 1;
            case "C":
                return 2;
            case "D":
                return 3;
        }
        System.out.println("cannot convert type to room");
        return -1;
    }

@AllArgsConstructor
@Getter
@Setter
private static class State {
    int cost;
    Amphipod[] corridor;
    Amphipod[][] rooms;

    @Override
    public String toString() {
        return "State -> " +
                "cost=" + cost + "\n" +
                corridorString() +
                roomsString() + "\n";
    }

    public String status() {
        return corridorString() +
                roomsString();
    }

    public String roomsString() {
        StringBuilder builder = new StringBuilder();
        builder.append("  ");
        for (int i = 0; i < rooms[0].length; i++) {
            builder.append("\n");
            builder.append("  ");
            for (int j = 0; j < rooms.length; j++) {
                if (rooms[j][i]!=null) {
                    builder.append(rooms[j][i].getType());
                } else {
                    builder.append(" ");
                }
                builder.append(" ");
            }

        }
        return builder.toString();
    }

    public String corridorString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Amphipod amphipod : corridor) {
            if (amphipod==null) {
                stringBuilder.append(".");
            } else {
                stringBuilder.append(amphipod.getType());
            }
        }
        return stringBuilder.toString();
    }
}

@Getter
@Setter
@ToString
private static class Amphipod {
    int cost;
    int moves;
    String type;
    int currentPosition;

    public Amphipod(String type, int currentPosition){
        this.type = type;
        this.currentPosition = currentPosition;
        this.cost = findCostByType();
        this.moves = 0;
    }

    private int findCostByType() {
        int cost = 0;
        switch(type) {
            case "A":
                cost = 1;
                break;
            case "B":
                cost = 10;
                break;
            case "C":
                cost = 100;
                break;
            case "D":
                cost = 1000;
                break;
        }
        return cost;
    }
}
}