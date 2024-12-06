package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.Food;
import csci.ooad.polymorphia.Maze;

import java.util.*;

public class HumanStrategy implements Strategy {

    final CommandFactory commandFactory = new CommandFactory();

    @Override
    public Command generateCommand(Character character) {

        List<CommandOption> availableCommands = availableCommandOptions(character);
        CommandOption choice = getTopLevelChoice(availableCommands, character);
        return getCommand(character, choice);
    }

    @Override
    public List<CommandOption> getOptions(Character character) {
        return availableCommandOptions(character);
    }

    Command getCommand(Character character, CommandOption choice) {
        Maze.Room currentRoom = character.getCurrentLocation();
        switch (choice) {
            case EAT:
                Food foodItem = currentRoom.selectRandomFood();
                return commandFactory.createEatCommand(character, foodItem);
            case FIGHT:
                Optional<Creature> creature = currentRoom.getHealthiestCreature();
                if (creature.isPresent()) {
                    return commandFactory.createFightCommand(character, creature.get());
                }
            case MOVE:
                return commandFactory.createMoveCommand(character, currentRoom.getRandomNeighbor());
            case WEAR_ARMOR:
                return commandFactory.wearArmor(character, currentRoom.selectRandomArmor());
            default:
                return commandFactory.createDoNothingCommand();
        }
    }

    public List<CommandOption> availableCommandOptions(Character character) {
        Maze.Room currentRoom = character.getCurrentLocation();

        List<CommandOption> options = new ArrayList<>();
        options.add(CommandOption.DO_NOTHING);
        if (currentRoom.hasNeighbors()) {
            options.add(CommandOption.MOVE);
        }
        if (currentRoom.hasFood()) {
            options.add(CommandOption.EAT);
        }
        if (currentRoom.hasArmor()) {
            options.add(CommandOption.WEAR_ARMOR);
        }
        if (currentRoom.hasLivingCreatures()) {
            options.add(CommandOption.FIGHT);
        }

        return options;
    }

    CommandOption getTopLevelChoice(List<CommandOption> options, Character character) {
        Maze.Room currentRoom = character.getCurrentLocation();

        System.out.print("You are in room " + currentRoom + "\n\n");
        int currentChoiceNumber = 1;
        for (CommandOption option : options) {
            System.out.println(currentChoiceNumber + ": " + option);
            currentChoiceNumber++;
        }
        while (true) {  // This exits the method if a valid option is selected
            System.out.print("Enter your option: ");
            Scanner scanner = new Scanner(System.in);
            try {
                int choiceNumber = Integer.parseInt(scanner.nextLine()) - 1;
                System.out.println("Your chosen option is " + options.get(choiceNumber).name());
                return options.get(choiceNumber);
            } catch (NumberFormatException e) {
                System.out.println("Invalid option selected");
            }
        }
    }
}
