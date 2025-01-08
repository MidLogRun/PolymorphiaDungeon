package csci.ooad.polymorphia;

import csci.ooad.polymorphia.characters.Adventurer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GateRoomTest {

    @Test
    public void testOpenGateRoom() {
        GateRoom room = new GateRoom("gate");
        RoomKey key = new RoomKey();

        room.openRoom(key);
        assertTrue(room.isOpened());
    }

    @Test
    public void testConnectCorrespondingRoom(){
        GateRoom room1 = new GateRoom("room1");
        GateRoom room2 = new GateRoom("room2");

        room1.connectCorrespondingGateRoom(room2);
        assertTrue(room1.hasNeighbor(room2));
    }

    @Test
    public void testCharacterUnableToOpenGateRoom(){
        Maze.Room room1 = new Maze.Room("room 1");
        GateRoom room2 = new GateRoom("room 2");
        room1.connect(room2);

        Adventurer character = new Adventurer("Guy");
        character.enterRoom(room1);
        character.getAction().execute(); //Manual test
    }

    @Test
    public void testCharacterOpensGateRoomWithKey(){
        Maze.Room room1 = new Maze.Room("room 1");
        GateRoom room2 = new GateRoom("room 2");
        room1.connect(room2);

        Adventurer character = new Adventurer("Guy");
        character.enterRoom(room1);

        RoomKey key = new RoomKey();
        assertFalse(key.isUsed());

        character.addKey(key);
        character.getAction().execute(); //Manual test

        assertTrue(key.isUsed());
    }




}
