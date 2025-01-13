package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.Food;
import csci.ooad.polymorphia.GateRoom;
import csci.ooad.polymorphia.Maze;
import csci.ooad.polymorphia.RoomKey;
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

        if (currentRoom.hasKey()) {
            //TODO INVENTORY SYSTEM HERE
            RoomKey key = currentRoom.popKey();
            return commandFactory.createPickUpItemCommand(character, key);
        }

        Maze.Room nextRoom = currentRoom.getRandomNeighbor();
        if (nextRoom != null) {
            return determineRoomChoices(character, nextRoom);
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

    Boolean roomHasKey(Maze.Room room) {
        return room.hasKey();
    }

    public Command determineRoomChoices(Character character, Maze.Room room) {
        logger.info("{} is determining their options for room {}", character.getName(), room.getName());
        if (room != null) {
            if (room.isGateRoom()) {
                if (room.isOpen()) {
                    return commandFactory.createMoveCommand(character, room);
                } else {
                    if (character.hasKey()) {
                        return commandFactory.createOpenRoomCommand(character, (GateRoom) room);
                    } else {
                        Maze.Room additionalNextRoom = room.getRandomNeighbor();
                        return commandFactory.createMoveCommand(character, additionalNextRoom);
                    }
                }
            } else {
                return commandFactory.createMoveCommand(character, room);
            }
        }
        return commandFactory.createDoNothingCommand();
    }

}
