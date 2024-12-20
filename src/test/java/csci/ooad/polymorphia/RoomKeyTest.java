package csci.ooad.polymorphia;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoomKeyTest {
    ArtifactFactory artifactFactory = new ArtifactFactory();

    @Test
    public void testRoomKeyAssignedValue(){
        Maze.Room room = new Maze.Room("gate room");
        RoomKey key = artifactFactory.createRoomKey(room);
        assertEquals(key.getRoom(), room);
    }

    @Test
    public void testUseKey(){
        Maze.Room room = new Maze.Room("gate room");
        RoomKey key = artifactFactory.createRoomKey(room);
        key.useKey();
        assertTrue(key.isUsed());
    }

}
