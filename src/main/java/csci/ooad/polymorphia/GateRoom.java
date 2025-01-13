package csci.ooad.polymorphia;

public class GateRoom extends Maze.Room {
    GateRoom correspondingRoom;
    private Boolean isOpened = false;

    public GateRoom(String name) {
        super(name);
    }

    public void connectCorrespondingGateRoom(GateRoom correspondingRoom) {
        if (this.correspondingRoom == null) {
            this.correspondingRoom = correspondingRoom;
            connect(correspondingRoom);
            correspondingRoom.connectCorrespondingGateRoom(this);
        }
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
        if (isOpened) return;

        if (!key.isUsed()){
            isOpened = true;
            correspondingRoom.openRoom(key);
            key.useKey();
        }
    }
}
