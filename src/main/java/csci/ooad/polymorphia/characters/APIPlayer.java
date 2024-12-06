package csci.ooad.polymorphia.characters;

import java.util.ArrayList;
import java.util.List;

public class APIPlayer extends Adventurer {

    String lastCommand;
    public APIPlayer(String name, Double initialHealth) {
        super(name, initialHealth, new ApiStrategy());
    }

    @Override
    public Boolean isApiPlayer(){
        return true;
    }

    public void setLastCommand(String lastCommand) {
        this.lastCommand = lastCommand;
    }

    public String getLastCommand() {
        return lastCommand;
    }

    @Override
    public Command getAction() {
        return this.strategy.generateCommand(this);
    }



}
