package csci.ooad.polymorphia.characters;

public class ApiStrategy extends HumanStrategy implements Strategy   {

    public Command generateCommandFromString(Character character, String commandString) {
        CommandOption choice = CommandOption.valueOf(commandString.toUpperCase());
        return getCommand(character, choice);
    }

    @Override
    public Command generateCommand(Character character) {
        APIPlayer apiPlayer =  (APIPlayer) character;
//I don't care how much this smells this should work for now
        return generateCommandFromString(character, apiPlayer.getLastCommand());
    }
}
