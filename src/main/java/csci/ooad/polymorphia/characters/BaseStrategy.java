package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.Food;
import csci.ooad.polymorphia.GateRoom;
import csci.ooad.polymorphia.Maze;
import csci.ooad.polymorphia.Polymorphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class BaseStrategy implements Strategy {
    private static final Logger logger = LoggerFactory.getLogger(BaseStrategy.class);

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
            if (nextRoom instanceof GateRoom) {
                //check if character has a key
                if (character.hasKey()) {
                    logger.info("{} opened {} with a key!", character.getName(), nextRoom.getName());
                    character.useKey();
                    return commandFactory.createMoveCommand(character, nextRoom);
                } else {
                    logger.info("Uh oh! {} does not have a key for {}\n \tDoing nothing instead.", character.getName(), nextRoom.getName());
                    return commandFactory.createDoNothingCommand();
                }
            } else {
                return commandFactory.createMoveCommand(character, nextRoom);
            }

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
