package csci.ooad.polymorphia;

import org.junit.jupiter.api.Test;

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

        room1.connectCorrespondingRoom(room2);
        assertTrue(room1.hasNeighbor(room2));
    }

    @Test
    public void testCreateGameOfConnectedMazes(){


        GateRoom room1 = new GateRoom("room1");
        GateRoom room2 = new GateRoom("room2");

        Maze maze1 = Maze.getNewBuilder()
                .createGateRoom("maze1 gateroom")
                .build();

        Maze maze2 = Maze.getNewBuilder()
                .createGateRoom("maze2 gateroom")
                .build();

        Polymorphia game = new Polymorphia(maze1);
        //Should Maze know about other mazes or should that be handled in Polymorphia.
        //Access a GateRoom in one maze to connect it to GateRoom in another

    }

}
