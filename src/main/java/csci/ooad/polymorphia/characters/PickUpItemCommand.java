package csci.ooad.polymorphia.characters;

import csci.ooad.polymorphia.RoomKey;

public class PickUpItemCommand extends PolymorphiaCommand    {
    RoomKey key;


    public PickUpItemCommand(Character character, RoomKey key) {
        super(character, "PICK UP ITEM");
        this.key = key;
    }

    @Override
    public void execute() {
        character.addKey(key);
    }
}
