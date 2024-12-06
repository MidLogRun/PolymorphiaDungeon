package csci.ooad.polymorphia.characters;

import java.util.List;

public interface Strategy {
    enum CommandOption {EAT, FIGHT, MOVE, WEAR_ARMOR, DO_NOTHING}
    Command generateCommand(Character character);
    default List<CommandOption> getOptions(Character character){
        return List.of();
    }

}
