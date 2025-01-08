package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.GateRoom;

public class OpenRoomCommand extends PolymorphiaCommand {
    final GateRoom room;

    public OpenRoomCommand(Character character, GateRoom room) {
        super(character, "OPEN");
        this.room = room;
    }

    @Override
    public void execute() {
        character.openRoom(room);
    }
}
