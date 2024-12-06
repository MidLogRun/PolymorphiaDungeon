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
    public List<HashMap<String, Object>> rooms = new ArrayList<>();
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
            HashMap<String, Object> room = new HashMap<>();
            room.put("name", roomName);
            room.put("neighbors", maze.getNeighborsOf(roomName));
            room.put("contents", maze.getContents(roomName));
            rooms.add(room);
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

   public List<HashMap<String, Object>> getRooms() {
        return rooms;
   }

    public String getStatusMessage() {
        return statusMessage;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"name\": \"").append(name).append("\",\n");
        sb.append("  \"turn\": ").append(turn).append(",\n");
        sb.append("  \"inMiddleOfTurn\": ").append(inMiddleOfTurn).append(",\n");
        sb.append("  \"gameOver\": ").append(gameOver).append(",\n");
        sb.append("  \"statusMessage\": \"").append(statusMessage).append("\",\n");

        sb.append("  \"livingAdventurers\": ").append(livingAdventurers.toString()).append(",\n");
        sb.append("  \"livingCreatures\": ").append(livingCreatures.toString()).append(",\n");

        sb.append("  \"rooms\": {\n");

        for (HashMap<String, Object> room : rooms) {
            sb.append("    {\n");
            sb.append("      \"name\": \"").append(room.get("name")).append("\",\n");
            sb.append("      \"neighbors\": ").append(room.get("neighbors").toString()).append(",\n");
            sb.append("      \"contents\": ").append(room.get("contents").toString()).append("\n");
            sb.append("    },\n");
        }
        if (!rooms.isEmpty()) sb.setLength(sb.length() - 2); // Remove trailing comma for the last room
        sb.append("\n  },\n");

        sb.append("  \"availableCommands\": ").append(availableCommands.toString()).append("\n");
        sb.append("}");
        return sb.toString();
    }

}
