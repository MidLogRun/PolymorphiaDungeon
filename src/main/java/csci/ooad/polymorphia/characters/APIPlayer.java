package csci.ooad.polymorphia.characters;

import java.util.ArrayList;
import java.util.List;

public class APIPlayer extends Adventurer {

    public APIPlayer(String name, Double initialHealth) {
        super(name, initialHealth, new HumanStrategy());
    }

    @Override
    public Boolean isApiPlayer(){
        return true;
    }


}
