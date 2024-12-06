package csci.ooad.polymorphia.characters;

import java.util.List;

public class DoNothingStrategy implements Strategy {

    @Override
    public Command generateCommand(Character character) {
        return new DoNothingCommand() {};
    }

}
