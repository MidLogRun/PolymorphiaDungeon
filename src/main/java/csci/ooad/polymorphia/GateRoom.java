package csci.ooad.polymorphia;

public class GateRoom extends Maze.Room {
    private Boolean isOpened = false;

    public GateRoom(String name) {
        super(name);
    }

    public void connectCorrespondingGateRoom(GateRoom correspondingRoom) {
        this.connect(correspondingRoom);
    }

    @Override
    public boolean isOpen(){
        return isOpened;
    }

    @Override
    public boolean isGateRoom(){
        return true;
    }

    public void openRoom(RoomKey key){
        if (!key.isUsed()){
            isOpened = true;
            key.useKey();
        }
    }
}
