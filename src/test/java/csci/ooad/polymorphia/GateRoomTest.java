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
}
