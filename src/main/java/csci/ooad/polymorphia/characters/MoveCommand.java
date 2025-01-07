package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.GateRoom;
import csci.ooad.polymorphia.Maze;

public class MoveCommand extends PolymorphiaCommand {
    final Maze.Room room;

    public MoveCommand(Character character, Maze.Room room) {
        super(character, "MOVE");

        if (room instanceof GateRoom){

        }

        this.room = room;
    }

    @Override
    public void execute() {
        character.move(room);
    }
}
