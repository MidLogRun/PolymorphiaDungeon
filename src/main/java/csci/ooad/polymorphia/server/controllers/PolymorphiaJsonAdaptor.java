package csci.ooad.polymorphia.server.controllers;

import csci.ooad.layout.intf.IMaze;
import csci.ooad.polymorphia.EventBus;
import csci.ooad.polymorphia.Maze;
import csci.ooad.polymorphia.Polymorphia;
import csci.ooad.polymorphia.characters.Adventurer;
import csci.ooad.polymorphia.characters.Creature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PolymorphiaJsonAdaptor {
    public String name;
    public int turn;
    public boolean inMiddleOfTurn;
    public boolean gameOver;
    public String statusMessage;
    public List<String> livingAdventurers = new ArrayList<>();
    public List<String> livingCreatures = new ArrayList<>();
    public HashMap<String, HashMap<String, List<String>>> rooms = new HashMap<>();
    public List<String> availableCommands = new ArrayList<>();

    public PolymorphiaJsonAdaptor(String gameName, Polymorphia polymorphia) {
        name = gameName;
        turn = polymorphia.getTurnNumber();
        gameOver = polymorphia.isOver();
        statusMessage = "Turn " + polymorphia.getTurnNumber() + " just ended.";
        livingAdventurers = polymorphia.getLivingAdventurers().stream().map(Adventurer::getName).toList();
        livingCreatures = polymorphia.getLivingCreatures().stream().map(Creature::getName).toList();

        IMaze maze = polymorphia.getMaze();

        List<String> roomNames = maze.getRoomNames().stream().toList();
        //Grab the neighbors and contents of a particular room and populate the rooms map:
        for (String roomName : roomNames) {
            List<String> neighbors = maze.getNeighborsOf(roomName).stream().toList();
            List<String> contents = maze.getContents(roomName).stream().toList();
            HashMap<String, List<String>> roomVariables = new HashMap<>();
            roomVariables.put("neighbors", neighbors);
            roomVariables.put("contents", contents);
            rooms.put(roomName, roomVariables);
        }
    }

    public String getName() {
        return name;
    }

    public int getTurn() {
        return turn;
    }

    public List<String> getLivingAdventurers() {
        return livingAdventurers;
    }

    public List<String> getLivingCreatures() {
        return livingCreatures;
    }

    public HashMap<String, HashMap<String, List<String>>> getRooms(){
        return rooms;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

}
