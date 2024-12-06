package csci.ooad.polymorphia.characters;

import java.util.List;

public interface Strategy {
    Command generateCommand(Character character);
    default List<HumanStrategy.CommandOption> getOptions(Character character){
        return List.of();
    }
}
