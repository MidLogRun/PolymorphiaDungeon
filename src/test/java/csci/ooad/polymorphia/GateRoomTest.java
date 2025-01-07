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

        room1.connectCorrespondingGateRoom(room2);
        assertTrue(room1.hasNeighbor(room2));
    }


}
