package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.Food;
import csci.ooad.polymorphia.Maze;

import java.util.List;
import java.util.Optional;

public class BaseStrategy implements Strategy {
    final CommandFactory commandFactory = new CommandFactory();

    public Command generateCommand(Character character) {
        Maze.Room currentRoom = character.getCurrentLocation();

        Optional<Creature> healthiestCreature = currentRoom.getHealthiestCreature();
        if (healthiestCreature.isPresent() && shouldFight(character)) {
            return commandFactory.createFightCommand(character, healthiestCreature.get());
        }

        if (currentRoom.hasFood()) {
            Food food = character.getCurrentLocation().selectRandomFood();
            return commandFactory.createEatCommand(character, food);
        }
        Maze.Room nextRoom = currentRoom.getRandomNeighbor();
        if (nextRoom != null) {
            return commandFactory.createMoveCommand(character, currentRoom.getRandomNeighbor());
        }
        return commandFactory.createDoNothingCommand();
    }

    @Override
    public List<CommandOption> getOptions(Character character) {
        return List.of();
    }

    Boolean shouldFight(Character character) {
        return character.creatureInRoomWithMe() && character.iAmHealthiestInRoom();
    }
}
