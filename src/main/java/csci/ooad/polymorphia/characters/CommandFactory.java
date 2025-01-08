package csci.ooad.polymorphia.characters;


import csci.ooad.polymorphia.*;

public class CommandFactory {

    public Command createMoveCommand(Character character, Maze.Room room) {
        return new MoveCommand(character, room);
    }

    public Command createFightCommand(Character character, Character opponent) {
        return new FightCommand(character, opponent);
    }

    public Command createEatCommand(Character character, Food foodItem) {
        return new EatCommand(character, foodItem);
    }

    public Command wearArmor(Character character, Armor armor) { return new WearCommand(character, armor); }

    public Command createDoNothingCommand() {
        return new DoNothingCommand();
    }

    public Command createOpenRoomCommand(Character character, GateRoom room) {
        return new OpenRoomCommand(character, room);
    }

    public Command createPickUpItemCommand(Character character, RoomKey key) {
        return new PickUpItemCommand(character, key);
    }
}
