package csci.ooad.polymorphia.characters;

public class ApiStrategy extends HumanStrategy implements Strategy   {

    public Command generateCommandFromString(Character character, String commandString) {
        CommandOption choice = CommandOption.valueOf(commandString);
        return getCommand(character, choice);
    }

    @Override
    public Command generateCommand(Character character) {
        return generateCommandFromString(character, character.getLastCommand());
    }
}
